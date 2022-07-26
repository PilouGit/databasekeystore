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

import io.github.pilougit.security.databasekeystore.DatabaseKeyStoreProvider;
import io.github.pilougit.security.databasekeystore.keystore.DatabaseKeyStoreLoadStoreParameter;
import io.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreRepository;
import io.github.pilougit.security.databasekeystore.keystore.service.CipheringKeyService;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class CoreTestProvider extends TestUtils {




    protected abstract DatabaseKeyStoreRepository getDatabaseKeyStore();
    protected abstract CipheringKeyService getCipheringKeyService();

    public KeyStore getKeyStore() throws KeyStoreException, NoSuchProviderException {
        return KeyStore.getInstance(DatabaseKeyStoreProvider.KEYSTORE, DatabaseKeyStoreProvider.PROVIDER_NAME);

    }
    protected void testSecretKey() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnrecoverableKeyException {

        KeyStore keystore = getKeyStore();
        keystore.load(new DatabaseKeyStoreLoadStoreParameter(this.getDatabaseKeyStore(),  getCipheringKeyService()));
        SecretKey key=this.generateSecretKey();
        keystore.setKeyEntry("pilou", key,"pilou".toCharArray(),null);

        Date date = keystore.getCreationDate("pilou");
        assertTrue(DateUtils.isSameDay(new Date(),date));
        SecretKey key2 = (SecretKey) keystore.getKey("pilou","pilou".toCharArray());
        assertEquals(key, key2);
    }
    protected void testRSAKey() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnrecoverableKeyException {

        KeyStore keystore = getKeyStore();
        keystore.load(new DatabaseKeyStoreLoadStoreParameter(this.getDatabaseKeyStore(),  getCipheringKeyService()));
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        Key publicKey=kp.getPublic();
        Key privateKey=kp.getPrivate();
        Certificate [] chain=new Certificate[1];
        chain[0]=generateSelfSignedX509Certificate();
        keystore.setKeyEntry("pilou_public", publicKey,"pilou_public".toCharArray(),chain);
        keystore.setKeyEntry("pilou_private", privateKey,"pilou_private".toCharArray(),chain);

        Date date = keystore.getCreationDate("pilou_public");
        assertTrue(DateUtils.isSameDay(new Date(),date));
         date = keystore.getCreationDate("pilou_private");
        assertTrue(DateUtils.isSameDay(new Date(),date));
        Key key2 =  keystore.getKey("pilou_public","pilou_public".toCharArray());
        assertEquals(publicKey, key2);
         key2 =  keystore.getKey("pilou_private","pilou_private".toCharArray());
        assertEquals(privateKey, key2);
    }
    protected void testECCKey() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnrecoverableKeyException, InvalidAlgorithmParameterException {

        KeyStore keystore = getKeyStore();
        keystore.load(new DatabaseKeyStoreLoadStoreParameter(this.getDatabaseKeyStore(),  getCipheringKeyService()));
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256r1");
        kpg.initialize(ecGenParameterSpec);
        KeyPair kp = kpg.generateKeyPair();
        Key publicKey=kp.getPublic();
        Key privateKey=kp.getPrivate();
        Certificate [] chain=new Certificate[1];
        chain[0]=generateSelfSignedX509Certificate();
        keystore.setKeyEntry("pilou_public", publicKey,"pilou_public".toCharArray(),chain);
        keystore.setKeyEntry("pilou_private", privateKey,"pilou_private".toCharArray(),chain);

        Date date = keystore.getCreationDate("pilou_public");
        assertTrue(DateUtils.isSameDay(new Date(),date));
        date = keystore.getCreationDate("pilou_private");
        assertTrue(DateUtils.isSameDay(new Date(),date));
        Key key2 =  keystore.getKey("pilou_public","pilou_public".toCharArray());
        assertEquals(publicKey, key2);
        key2 =  keystore.getKey("pilou_private","pilou_private".toCharArray());
        assertEquals(privateKey, key2);
    }
        protected void testCertificate() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Certificate certificate=generateSelfSignedX509Certificate();
        KeyStore keystore = getKeyStore();
        keystore.load(new DatabaseKeyStoreLoadStoreParameter(this.getDatabaseKeyStore(), getCipheringKeyService()));
        keystore.setCertificateEntry("pilou", certificate);

        Date date = keystore.getCreationDate("pilou");
        assertTrue(DateUtils.isSameDay(new Date(),date));
        Certificate certificate2 = keystore.getCertificate("pilou");
        assertEquals(certificate2, certificate);


    }
}
