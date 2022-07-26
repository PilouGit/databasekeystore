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

package io.github.pilougit.security.databasekeystore;

import lombok.extern.slf4j.Slf4j;

import java.security.Provider;

@Slf4j
public class DatabaseKeyStoreProvider extends Provider {

    public  static final String PROVIDER_NAME = "DatabaseKeyStoreProvider";
    public static final String KEYSTORE="KeyStore";
    protected static final double version = 1.0d;
    protected static final String PROVIDER_INFO = "Information";


    public DatabaseKeyStoreProvider() {
        super(PROVIDER_NAME, version, PROVIDER_INFO);
        DatabaseKeyStoreProvider.this.putService(new DatabaseKeyStoreService(this,   DatabaseKeyStoreProvider.KEYSTORE,DatabaseKeyStoreProvider.KEYSTORE
              , "io.github.pilougit.security.DatabaseKeyStoreService", null, null));

    }


}
