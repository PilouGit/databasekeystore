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
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

public interface CipheringKeyService {

    public SecretKey generateSecretKey(char[] password, byte[] salt) throws CipheringKeyServiceException;

    public Pair<byte[], byte[]> encrypt(SecretKey secretKey, byte[] salt, byte[] data) throws CipheringKeyServiceException;

    public byte[] decrypt(SecretKey secretKey, byte[] ivSource, byte[] salt, byte[] cipherMessage) throws CipheringKeyServiceException;


    public SecureRandom getSecureRandom();
}
