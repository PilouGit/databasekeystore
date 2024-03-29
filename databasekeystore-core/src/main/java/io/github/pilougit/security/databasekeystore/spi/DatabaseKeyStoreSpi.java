/*
 * Copyright 2022  Pierre-Emmanuel Gros aka Pilou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pilougit.security.databasekeystore.spi;

import io.github.pilougit.security.databasekeystore.keystore.DatabaseKeyStoreLoadStoreParameter;
import io.github.pilougit.security.databasekeystore.keystore.exceptions.CipheringKeyServiceException;
import io.github.pilougit.security.databasekeystore.keystore.exceptions.DatabaseKeyStoreRepositoryException;
import io.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreCertificate;
import io.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;
import io.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreProtectedKey;
import io.github.pilougit.security.databasekeystore.keystore.model.TypeCipheredKey;
import io.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreRepositoryTransaction;
import io.github.pilougit.security.databasekeystore.keystore.service.CipheringKeyService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
public class DatabaseKeyStoreSpi extends KeyStoreSpi {
    private DatabaseKeyStoreLoadStoreParameter databaseKeyStoreLoadStoreParameter;

    @Override
    public Key engineGetKey(String alias, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        log.trace("DatabaseKeyStoreSpi::engineGetKey({},{})", alias, password);
        DatabaseKeyStoreEntry entry = null;
        try {
            entry = this.getDatabaseKeyStoreEntryByAlias(alias);
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }
        Key result = null;
        if (entry != null && entry.isKeyEntry()) {
            try {
                DatabaseKeyStoreProtectedKey cipherKey = entry.getCipherKey();

                @NonNull CipheringKeyService cipheringService = this.databaseKeyStoreLoadStoreParameter.getCipheringKeyService();
                Base64.Decoder base64Encoder = Base64.getDecoder();
                byte[] saltKey = base64Encoder.decode(cipherKey.getSaltKey());
                SecretKey secretKey = cipheringService.generateSecretKey(password, saltKey);
                byte[] aad = base64Encoder.decode(cipherKey.getSaltCipher());
                byte[] iv = base64Encoder.decode(cipherKey.getIvCiphedKey());
                byte[] cipheredData = base64Encoder.decode(cipherKey.getCipheredData());
                byte[] clearKey = cipheringService.decrypt(secretKey, iv, aad, cipheredData);

                String algorightm = cipherKey.getAlgorithmCipheredData();
                @NonNull TypeCipheredKey typeCipheredKey = cipherKey.getTypeCipheredKey();
                switch (typeCipheredKey) {
                    case SYMETRIC_KEY: {
                        result = new SecretKeySpec(clearKey, 0, clearKey.length, algorightm);
                        break;
                    }
                    case PRIVATE_KEY: {
                        KeyFactory keyFactory = KeyFactory.getInstance(algorightm);
                        PKCS8EncodedKeySpec privateKey = new PKCS8EncodedKeySpec (clearKey);
                        result = keyFactory.generatePrivate(privateKey);
                        break;
                    }
                    case PUBLIC_KEY: {
                        KeyFactory keyFactory = KeyFactory.getInstance(algorightm);
                        X509EncodedKeySpec publicKey = new X509EncodedKeySpec(clearKey);
                        result = keyFactory.generatePublic(publicKey);
                        break;
                    }
                }
                //  KeyFactory.getInstance("RSA").
            } catch (CipheringKeyServiceException e) {
                throw new UnrecoverableKeyException();
            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }

        }
        return result;

    }

    @Override
    public Certificate[] engineGetCertificateChain(String alias) {

        DatabaseKeyStoreEntry entry = null;
        try {
            entry = this.getDatabaseKeyStoreEntryByAlias(alias);
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }
        Certificate[] result = null;
        if (entry != null && entry.getChain() != null) {
            result = new Certificate[entry.getChain().size()];
            for (int i = 0; i < result.length; i++) {
                try {
                    result[i] = entry.getChain().get(i).convertToCertificate();
                } catch (CertificateException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    @Override
    public Certificate engineGetCertificate(String alias) {

        DatabaseKeyStoreEntry entry = null;
        try {
            entry = getDatabaseKeyStoreEntryByAlias(alias);
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }
        Certificate result = null;
        if (entry != null && entry.isCertEntry()) {
            DatabaseKeyStoreCertificate databaseKeyStoreCertificate = entry.getCert();
            try {
                result = databaseKeyStoreCertificate.convertToCertificate();
            } catch (CertificateException e) {
                throw new RuntimeException(e);
            }
        }


        return result;
    }

    @Override
    public Date engineGetCreationDate(String alias) {
        log.trace("DatabaseKeyStoreSpi::engineGetCreationDate({})", alias);
        DatabaseKeyStoreEntry entry = null;
        try {
            entry = getDatabaseKeyStoreEntryByAlias(alias);
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }
        return entry != null ? Date.from(entry.getCreated().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }

    @Override
    public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] certificates) throws KeyStoreException {
        DatabaseKeyStoreRepositoryTransaction transaction=null;
        try {
            transaction = this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().beginTransaction();
            @NonNull CipheringKeyService cipheringService = this.databaseKeyStoreLoadStoreParameter.getCipheringKeyService();
            byte[] saltKey = new byte[8];
            byte[] aad = new byte[8];
            SecureRandom random = cipheringService.getSecureRandom();
            random.nextBytes(saltKey);
            random.nextBytes(aad);
            SecretKey secretKey = cipheringService.generateSecretKey(password, saltKey);

            String algorithm = key.getAlgorithm();
            Pair<byte[], byte[]> dataAndIv = cipheringService.encrypt(secretKey, aad, key.getEncoded());
            Base64.Encoder base64Encoder = Base64.getEncoder();

            DatabaseKeyStoreEntry keyStoreEntry = this.getDatabaseKeyStoreEntryByAlias(alias);
            if (keyStoreEntry == null) {
                keyStoreEntry = new DatabaseKeyStoreEntry(alias, LocalDate.now());
            }
            TypeCipheredKey typeCipheredKey = TypeCipheredKey.SYMETRIC_KEY;
            if (key instanceof PrivateKey) {
                typeCipheredKey = TypeCipheredKey.PRIVATE_KEY;
            } else {
                if (key instanceof PublicKey) {
                    typeCipheredKey = TypeCipheredKey.PUBLIC_KEY;
                }
            }

            DatabaseKeyStoreProtectedKey protectedKey = new DatabaseKeyStoreProtectedKey(base64Encoder.encodeToString(saltKey),
                    base64Encoder.encodeToString(dataAndIv.getRight()),
                    base64Encoder.encodeToString(aad),
                    base64Encoder.encodeToString(dataAndIv.getLeft()),
                    algorithm, typeCipheredKey);
            keyStoreEntry.setCipherKey(protectedKey);
            this.setCertificateChain(keyStoreEntry, certificates);
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().store(keyStoreEntry);
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().commitTransaction(transaction);

        } catch (CipheringKeyServiceException | CertificateEncodingException | DatabaseKeyStoreRepositoryException e) {
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().rollBackTransaction(transaction);
            throw new KeyStoreException(e);
        }
    }

    protected void setCertificateChain(DatabaseKeyStoreEntry entry, Certificate[] certificates) throws CertificateEncodingException {
        if (certificates != null) {
            entry.setChain(convertCertificates(certificates));
        }
    }

    protected List<DatabaseKeyStoreCertificate> convertCertificates(Certificate[] certificates) throws CertificateEncodingException {
        List<DatabaseKeyStoreCertificate> result = new ArrayList<>();
        for (Certificate certificate : certificates) result.add(new DatabaseKeyStoreCertificate(certificate));
        return result;
    }


    @Override
    public void engineSetKeyEntry(String alias, byte[] bytes, Certificate[] certificates) throws KeyStoreException {
        throw new UnsupportedOperationException("DatabaseKeyStoreSpi::engineSetKeyEntry(String,bytes[],certificates) Not implemented");

    }

    @Override
    public void engineSetCertificateEntry(String alias, Certificate certificate) throws KeyStoreException {

        DatabaseKeyStoreRepositoryTransaction transaction=null;
        try {
             transaction = this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().beginTransaction();

            DatabaseKeyStoreEntry entry = getDatabaseKeyStoreEntryByAlias(alias);

            DatabaseKeyStoreCertificate databaseKeyStoreCertificate = new DatabaseKeyStoreCertificate(certificate);
            if (entry == null) {
                entry = new DatabaseKeyStoreEntry(alias, LocalDate.now());
            }
            entry.setCert(databaseKeyStoreCertificate);
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().store(entry);
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().commitTransaction(transaction);
        } catch (CertificateEncodingException | DatabaseKeyStoreRepositoryException e) {
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().rollBackTransaction(transaction);

            throw new KeyStoreException(e);
        }

    }

    @Override
    public void engineDeleteEntry(String alias) throws KeyStoreException {
        DatabaseKeyStoreRepositoryTransaction transaction=null;
        try {
             transaction = this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().beginTransaction();

            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().delete(alias);
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().commitTransaction(transaction);
        } catch (DatabaseKeyStoreRepositoryException e) {
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().rollBackTransaction(transaction);
            throw new KeyStoreException(e);
        }
    }

    @Override
    public Enumeration<String> engineAliases() {
        try {
            return this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().listAlias();
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean engineContainsAlias(String alias) {
        try {
            return getDatabaseKeyStoreEntryByAlias(alias) != null;
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int engineSize() {
        try {
            return this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().size();
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean engineIsKeyEntry(String alias) {
        try {
            return this.getDatabaseKeyStoreEntryByAlias(alias).isKeyEntry();
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean engineIsCertificateEntry(String alias) {
        DatabaseKeyStoreEntry keyStoreEntry = null;
        try {
            keyStoreEntry = getDatabaseKeyStoreEntryByAlias(alias);
        } catch (DatabaseKeyStoreRepositoryException e) {
            throw new RuntimeException(e);
        }

        return keyStoreEntry != null && keyStoreEntry.isCertEntry();
    }

    @Override
    public String engineGetCertificateAlias(Certificate certificate) {
        return null;
    }

    @Override
    public void engineStore(OutputStream outputStream, char[] chars) throws IOException, NoSuchAlgorithmException, CertificateException {
        throw new UnsupportedOperationException("DatabaseKeyStoreSpi::engineStore(InputStream,chars[]) Not implemented");

    }

    protected DatabaseKeyStoreEntry getDatabaseKeyStoreEntryByAlias(String alias) throws DatabaseKeyStoreRepositoryException {
        return this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().getByAlias(alias);
    }

    @Override
    public void engineLoad(InputStream inputStream, char[] chars) throws IOException, NoSuchAlgorithmException, CertificateException {
        throw new UnsupportedOperationException("DatabaseKeyStoreSpi::engineLoad(InputStream,chars[]) Not implemented");
    }

    @Override
    public void engineLoad(KeyStore.LoadStoreParameter param) throws IOException, NoSuchAlgorithmException, CertificateException {
        //super.engineLoad(param);
        if (param instanceof DatabaseKeyStoreLoadStoreParameter) {
            this.databaseKeyStoreLoadStoreParameter = (DatabaseKeyStoreLoadStoreParameter) param;
        }
    }
}
