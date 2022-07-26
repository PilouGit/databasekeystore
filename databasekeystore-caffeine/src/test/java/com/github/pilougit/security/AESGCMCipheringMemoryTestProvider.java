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

package io.github.pilougit.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.pilougit.security.databasekeystore.keystore.DatabaseKeyStoreLoadStoreParameter;
import io.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;
import io.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreCacheRepository;
import io.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreMemoryRepository;
import io.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreRepository;
import io.github.pilougit.security.databasekeystore.keystore.service.AESGcmCipheringKeyService;
import io.github.pilougit.security.databasekeystore.keystore.service.CipheringKeyService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AESGCMCipheringMemoryTestProvider extends CoreTestProvider {
    protected DatabaseKeyStoreRepository getDatabaseKeyStore()
    {

        Cache<String, DatabaseKeyStoreEntry> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build();

        return new DatabaseKeyStoreCacheRepository(cache,new DatabaseKeyStoreMemoryRepository());
    }

    @Override
    protected CipheringKeyService getCipheringKeyService() {
        return new AESGcmCipheringKeyService();
    }

    @Test
    public void testCertificate() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
            java.security.cert.Certificate certificate=generateSelfSignedX509Certificate();
            KeyStore keystore = getKeyStore();
            keystore.load(new DatabaseKeyStoreLoadStoreParameter(this.getDatabaseKeyStore(), getCipheringKeyService()));
            keystore.setCertificateEntry("pilou", certificate);


            Date date = keystore.getCreationDate("pilou");
            assertTrue(DateUtils.isSameDay(new Date(),date));
            Certificate certificate2 = keystore.getCertificate("pilou");
            assertEquals(certificate2, certificate);
            Certificate certificate3 = keystore.getCertificate("pilou");
            assertEquals(certificate3, certificate);



    }
    @Test
    public void testSecretKey() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnrecoverableKeyException {
        super.testSecretKey();
    }
    @Test
    public void testRSAKey() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnrecoverableKeyException {
        super.testRSAKey();
    }
    @Test
    public void testECCKey() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnrecoverableKeyException, InvalidAlgorithmParameterException {
        super.testECCKey();
    }

}
