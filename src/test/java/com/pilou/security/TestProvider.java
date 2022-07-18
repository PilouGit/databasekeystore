/*
 *
 *  *
 *  * Copyright 2022 Pierre-Emmanuel Gros.
 *  *
 *  * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 *  * use this file except in compliance with the License. You may obtain a copy of
 *  * the License at
 *  *
 *  * https://www.gnu.org/licenses/lgpl-3.0.html
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  * License for the specific language governing permissions and limitations under
 *  * the License.
 *
 */

package com.pilou.security;

import com.pilou.security.databasekeystore.DatabaseKeyStoreProvider;
import org.junit.Test;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class TestProvider {

    @Test
    public void testLoadTestProvider() throws KeyStoreException, NoSuchProviderException {
        Security.addProvider(new DatabaseKeyStoreProvider());
        KeyStore keystore = KeyStore.getInstance("DatabaseKeyStoreProvider", "DatabaseKeyStoreProvider");
        KeyStore.LoadStoreParameter parameter;


    }
}
