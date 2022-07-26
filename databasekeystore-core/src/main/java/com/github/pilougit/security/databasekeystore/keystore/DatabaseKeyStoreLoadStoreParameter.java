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

package com.github.pilougit.security.databasekeystore.keystore;

import com.github.pilougit.security.databasekeystore.keystore.repository.DatabaseKeyStoreRepository;
import com.github.pilougit.security.databasekeystore.keystore.service.CipheringKeyService;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyStore;

@Slf4j
@Data
public class DatabaseKeyStoreLoadStoreParameter implements KeyStore.LoadStoreParameter {


    protected DatabaseKeyStoreProtectionParameter protectionParameter;
    @NonNull
    protected DatabaseKeyStoreRepository databaseKeyStoreRepository;
    @NonNull
    protected CipheringKeyService cipheringKeyService;


}
