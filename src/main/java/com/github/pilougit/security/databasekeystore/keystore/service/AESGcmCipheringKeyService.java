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

package com.github.pilougit.security.databasekeystore.keystore.service;

import com.github.pilougit.security.databasekeystore.keystore.exceptions.CipheringKeyServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Slf4j
@Data
public class AESGcmCipheringKeyService implements CipheringKeyService{

    private final SecureRandom secureRandom = new SecureRandom();
    private final static int GCM_IV_LENGTH = 12;


    @Override
    public SecretKey generateSecretKey(char[] password, byte[] salt) throws CipheringKeyServiceException {
        try {
            SecretKeyFactory factory = null;
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        return secret;
        } catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
            throw new CipheringKeyServiceException(e);
        }
    }

    @Override
    public Pair<byte[],byte[]> encrypt(SecretKey secretKey, byte[] salt, byte[] data) throws CipheringKeyServiceException {

        try {
            byte[] ivSource = new byte[GCM_IV_LENGTH]; //NEVER REUSE THIS IV WITH SAME KEY
            secureRandom.nextBytes(ivSource);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, ivSource); //128 bit auth tag length
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        if (data!=null) cipher.updateAAD(salt);
        byte[] cipherText = cipher.doFinal(data);
        return Pair.of(cipherText,ivSource);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CipheringKeyServiceException(e);
        }
    }

    @Override
    public byte[] decrypt(SecretKey secretKey, byte[] ivSource, byte[] salt, byte[] cipherMessage) throws CipheringKeyServiceException {
        try {
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        AlgorithmParameterSpec parameterSpec = new GCMParameterSpec(128, ivSource);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        if (salt != null) {
            cipher.updateAAD(salt);
        }
        return cipher.doFinal(cipherMessage);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
    InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
        throw new CipheringKeyServiceException(e);
    }
    }
}
