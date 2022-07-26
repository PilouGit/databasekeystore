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

import com.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreMemoryRepository;
import com.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreRepository;
import com.github.pilougit.security.databasekeystore.keystore.service.AESGcmCipheringKeyService;
import com.github.pilougit.security.databasekeystore.keystore.service.CipheringKeyService;
import org.junit.Test;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class AESGCMCipheringMemoryTestProvider extends CoreTestProvider {
    protected DatabaseKeyStoreRepository getDatabaseKeyStore()
    {
        return new DatabaseKeyStoreMemoryRepository();
    }

    @Override
    protected CipheringKeyService getCipheringKeyService() {
        return new AESGcmCipheringKeyService();
    }

    @Test
    public void testCertificate() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        super.testCertificate();
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
