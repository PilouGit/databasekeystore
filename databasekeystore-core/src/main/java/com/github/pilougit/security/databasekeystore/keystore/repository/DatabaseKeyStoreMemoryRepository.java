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

package com.github.pilougit.security.databasekeystore.keystore.repository;

import com.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DatabaseKeyStoreMemoryRepository implements DatabaseKeyStoreRepository {
    protected Map<String, DatabaseKeyStoreEntry> entry = new HashMap<>();

    @Override
    public DatabaseKeyStoreEntry getByAlias(String alias) {
        return entry.get(alias);
    }

    @Override
    public int size() {
        return entry.size();
    }

    @Override
    public Enumeration<String> listAlias() {
        return Collections.enumeration(entry.keySet());
    }

    @Override
    public void delete(String alias) {
        this.entry.remove(alias);
    }

    @Override
    public void store(DatabaseKeyStoreEntry databaseKeyStoreEntry) {
        this.entry.put(databaseKeyStoreEntry.getLabel(), databaseKeyStoreEntry);
    }
}
