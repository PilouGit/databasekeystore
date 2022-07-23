/*
 *
 *
 * Copyright 2022 Pierre-Emmanuel Gros.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.pilou.security.databasekeystore.spi;

import com.pilou.security.databasekeystore.keystore.DatabaseKeyStoreLoadStoreParameter;
import com.pilou.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

@Slf4j
public class DatabaseKeyStoreSpi extends KeyStoreSpi {
    private DatabaseKeyStoreLoadStoreParameter databaseKeyStoreLoadStoreParameter;

    @Override
    public Key engineGetKey(String alias, char[] chars) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        log.trace("DatabaseKeyStoreSpi::engineGetKey({},{})",alias,chars);
        return null;
    }

    @Override
    public Certificate[] engineGetCertificateChain(String s) {
        return new Certificate[0];
    }

    @Override
    public Certificate engineGetCertificate(String s) {



        return null;
    }

    @Override
    public Date engineGetCreationDate(String alias) {
        log.trace("DatabaseKeyStoreSpi::engineGetCreationDate({})",alias);
        DatabaseKeyStoreEntry entry = getDatabaseKeyStoreEntryByAlias(alias);
        return entry!=null?Date.from(entry.getCreated().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
    }

    @Override
    public void engineSetKeyEntry(String alias, Key key, char[] chars, Certificate[] certificates) throws KeyStoreException {

    }

    @Override
    public void engineSetKeyEntry(String s, byte[] bytes, Certificate[] certificates) throws KeyStoreException {

    }

    @Override
    public void engineSetCertificateEntry(String alias, Certificate certificate) throws KeyStoreException {

        try {
            DatabaseKeyStoreEntry entry = getDatabaseKeyStoreEntryByAlias(alias);
            String base64Certificate=Base64.getEncoder().encodeToString(certificate.getEncoded());
            if (entry==null)
            {
                entry=new DatabaseKeyStoreEntry(alias, LocalDate.now());

            }else
            {

            }
            entry.setCert(base64Certificate);
            this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().store(entry);
        } catch (CertificateEncodingException e) {
            throw new KeyStoreException(e);
        }

    }

    @Override
    public void engineDeleteEntry(String alias) throws KeyStoreException {
        this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().delete(alias);
    }

    @Override
    public Enumeration<String> engineAliases() {
        return this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().listAlias();
    }

    @Override
    public boolean engineContainsAlias(String alias) {
        return getDatabaseKeyStoreEntryByAlias(alias)!=null;
    }

    @Override
    public int engineSize() {
        return this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().size();
    }

    @Override
    public boolean engineIsKeyEntry(String s) {
        return false;
    }

    @Override
    public boolean engineIsCertificateEntry(String alias) {
        DatabaseKeyStoreEntry keyStoreEntry = getDatabaseKeyStoreEntryByAlias(alias);

        return keyStoreEntry!=null && keyStoreEntry.isCertEntry();
    }

    @Override
    public String engineGetCertificateAlias(Certificate certificate) {
        return null;
    }

    @Override
    public void engineStore(OutputStream outputStream, char[] chars) throws IOException, NoSuchAlgorithmException, CertificateException {
        throw new UnsupportedOperationException("DatabaseKeyStoreSpi::engineStore(InputStream,chars[]) Not implemented");

    }

    protected DatabaseKeyStoreEntry getDatabaseKeyStoreEntryByAlias(String alias)
    {
        return this.databaseKeyStoreLoadStoreParameter.getDatabaseKeyStoreRepository().getByAlias(alias);
    }
    @Override
    public void engineLoad(InputStream inputStream, char[] chars) throws IOException, NoSuchAlgorithmException, CertificateException {
    throw new UnsupportedOperationException("DatabaseKeyStoreSpi::engineLoad(InputStream,chars[]) Not implemented");
    }

    @Override
    public void engineLoad(KeyStore.LoadStoreParameter param) throws IOException, NoSuchAlgorithmException, CertificateException {
        //super.engineLoad(param);
        if (param instanceof DatabaseKeyStoreLoadStoreParameter)
        {
            this.databaseKeyStoreLoadStoreParameter= (DatabaseKeyStoreLoadStoreParameter) param;
        }else
        {
            throw new UnsupportedOperationException("DatabaseKeyStoreSpi::engineLoad(KeyStore.LoadStoreParameter ) param not instanceof DatabaseKeyStoreLoadStoreParameter");

        }
    }
}
