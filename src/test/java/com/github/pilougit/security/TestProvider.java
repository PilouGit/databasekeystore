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

package com.github.pilougit.security;

import com.github.pilougit.security.databasekeystore.keystore.DatabaseKeyStoreLoadStoreParameter;
import com.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreMemoryRepository;
import com.github.pilougit.security.databasekeystore.keystore.service.AESGcmCipheringKeyService;
import com.github.pilougit.security.databasekeystore.DatabaseKeyStoreProvider;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class TestProvider {
    private static X509Certificate certificate;

    public static X509Certificate generateSelfSignedX509Certificate() throws CertificateEncodingException, InvalidKeyException, IllegalStateException,
            NoSuchProviderException, NoSuchAlgorithmException, SignatureException {

        // generate a key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(4096, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // build a certificate generator
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        X500Principal dnName = new X500Principal("cn=example");

        // add some options
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setSubjectDN(new X509Name("dc=name"));
        certGen.setIssuerDN(dnName); // use the same
        // yesterday
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        // in 2 years
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000));
        certGen.setPublicKey(keyPair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        certGen.addExtension(X509Extensions.ExtendedKeyUsage, true,
                new ExtendedKeyUsage(KeyPurposeId.id_kp_timeStamping));

        // finally, sign the certificate with the private key of the same KeyPair
        X509Certificate cert = certGen.generate(keyPair.getPrivate(), "BC");
        return cert;
    }


    @BeforeClass
    public static void addBouncyCastleAsSecurityProvider() throws CertificateEncodingException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        certificate=generateSelfSignedX509Certificate();
    }
    @Test
    public void testLoadTestProvider() throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException {
            Security.addProvider(new DatabaseKeyStoreProvider());
        KeyStore keystore = KeyStore.getInstance("DatabaseKeyStoreProvider", "DatabaseKeyStoreProvider");
        keystore.load(new DatabaseKeyStoreLoadStoreParameter( new DatabaseKeyStoreMemoryRepository(),new AESGcmCipheringKeyService()));
        keystore.setCertificateEntry("pilou",certificate);

        Date date=keystore.getCreationDate("pilou");
        System.err.println(date);
        Certificate certificate2 = keystore.getCertificate("pilou");
        System.err.println(certificate2);

    }
}
