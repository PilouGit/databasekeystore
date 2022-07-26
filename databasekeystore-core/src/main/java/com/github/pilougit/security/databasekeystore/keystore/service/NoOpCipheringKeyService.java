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

package com.github.pilougit.security.databasekeystore.keystore.service;

import com.github.pilougit.security.databasekeystore.keystore.exceptions.CipheringKeyServiceException;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class NoOpCipheringKeyService implements CipheringKeyService{
    @Override
    public SecretKey generateSecretKey(char[] password, byte[] salt) throws CipheringKeyServiceException {
        return null;
    }

    @Override
    public Pair<byte[], byte[]> encrypt(SecretKey secretKey, byte[] salt, byte[] data) throws CipheringKeyServiceException {
        return Pair.of(data,new byte[0]);
    }

    @Override
    public byte[] decrypt(SecretKey secretKey, byte[] ivSource, byte[] salt, byte[] cipherMessage) throws CipheringKeyServiceException {
        return cipherMessage;
    }

    @Override
    public SecureRandom getSecureRandom() {
        return new SecureRandom();
    }
}
