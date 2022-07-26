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

package io.github.pilougit.security.databasekeystore.keystore.repository;

import io.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DatabaseKeyStoreMemoryRepository implements DatabaseKeyStoreRepository<DatabaseKeyStoreMemoryRepositoryTransaction>{
    protected Map<String, DatabaseKeyStoreEntry> entry = new HashMap<>();

    @Override
    public DatabaseKeyStoreMemoryRepositoryTransaction beginTransaction() {
        return new DatabaseKeyStoreMemoryRepositoryTransaction();
    }

    @Override
    public void commitTransaction(DatabaseKeyStoreMemoryRepositoryTransaction transaction) {

    }

    @Override
    public void rollBackTransaction(DatabaseKeyStoreMemoryRepositoryTransaction transaction) {

    }



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
