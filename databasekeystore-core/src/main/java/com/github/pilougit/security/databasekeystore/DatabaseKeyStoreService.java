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

package com.github.pilougit.security.databasekeystore;

import com.github.pilougit.security.databasekeystore.spi.DatabaseKeyStoreSpi;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.cert.CertStoreParameters;
import java.util.List;
import java.util.Map;

public class DatabaseKeyStoreService extends Provider.Service {
    public DatabaseKeyStoreService(Provider provider, String type, String algorithm, String className, List<String> aliases, Map<String, String> attributes) {
        super(provider, type, algorithm, className, aliases, attributes);
    }

    @Override
    public Object newInstance(Object constructorParameter) throws NoSuchAlgorithmException {
        String type = this.getType();
        String algo = this.getAlgorithm();
        if (type.equals("KeyStore") && algo.equals("DatabaseKeyStoreProvider")) {
            if (constructorParameter != null && !(constructorParameter instanceof CertStoreParameters)) {
                throw new InvalidParameterException("constructorParameter must be instanceof CertStoreParameters");
            } else {
                try {
                    return new DatabaseKeyStoreSpi();
                } catch (Exception var5) {
                    throw new NoSuchAlgorithmException("Error constructing " + type + " for " + algo + " using JdkLDAP", var5);
                }
            }
        } else {
            throw new ProviderException("No impl for " + algo + " " + type);
        }
    }
}
