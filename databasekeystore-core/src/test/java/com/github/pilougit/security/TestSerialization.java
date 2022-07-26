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

package com.github.pilougit.security;

import com.github.pilougit.security.databasekeystore.keystore.exceptions.DatabaseKeyStoreEntrySerialization;
import com.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreCertificate;
import com.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;
import com.github.pilougit.security.databasekeystore.keystore.service.DatabaseKeyStoreEntrySerializationService;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class TestSerialization extends TestUtils{

    @Test
    public void testSerialization() throws CertificateEncodingException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, DatabaseKeyStoreEntrySerialization {
        DatabaseKeyStoreCertificate databaseKeyStoreCertificate = new DatabaseKeyStoreCertificate(super.generateSelfSignedX509Certificate());
        DatabaseKeyStoreEntry entry = new DatabaseKeyStoreEntry("test", LocalDate.now());
        entry.setCert(databaseKeyStoreCertificate);
        DatabaseKeyStoreEntrySerializationService service=new DatabaseKeyStoreEntrySerializationService();
        String serializeVersion=service.serializeDatabaseKeyStoreEntry(entry);

        DatabaseKeyStoreEntry read= service.unserializeDatabaseKeyStoreEntry(serializeVersion);
        assertEquals(read.getCreated(),entry.getCreated());
        assertEquals(read.getLabel(),entry.getLabel());

    }
}
