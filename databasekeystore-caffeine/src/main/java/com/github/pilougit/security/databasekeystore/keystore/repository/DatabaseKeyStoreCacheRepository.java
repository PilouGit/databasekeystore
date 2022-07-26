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

package com.github.pilougit.security.databasekeystore.keystore.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.pilougit.security.databasekeystore.keystore.exceptions.DatabaseKeyStoreRepositoryException;
import com.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Data
public class DatabaseKeyStoreCacheRepository implements DatabaseKeyStoreRepository<DatabaseKeyStoreCacheRepositoryTransaction>{
    @NonNull
    protected Cache<String,DatabaseKeyStoreEntry> keystoreCache;
    @NonNull
    protected DatabaseKeyStoreRepository delegateRepository;


    @Override
    public DatabaseKeyStoreCacheRepositoryTransaction beginTransaction() {

        return new DatabaseKeyStoreCacheRepositoryTransaction(delegateRepository.beginTransaction());
    }

    @Override
    public void commitTransaction(DatabaseKeyStoreCacheRepositoryTransaction transaction) {

        delegateRepository.commitTransaction(transaction.getTransaction());
    }
    @Override
    public void rollBackTransaction(DatabaseKeyStoreCacheRepositoryTransaction transaction) {
        delegateRepository.rollBackTransaction(transaction.getTransaction());
    }
    @Override
    public DatabaseKeyStoreEntry getByAlias(String alias) throws DatabaseKeyStoreRepositoryException{

        DatabaseKeyStoreEntry entry=  this.keystoreCache.getIfPresent(alias);
        if (entry==null) {
          entry=this.delegateRepository.getByAlias(alias);
         if (entry!=null) this.keystoreCache.put(alias,entry);
        }
        return entry;
    }

    @Override
    public int size() throws DatabaseKeyStoreRepositoryException {
            return delegateRepository.size();
    }

    @Override
    public Enumeration<String> listAlias() throws DatabaseKeyStoreRepositoryException {
        return delegateRepository.listAlias();
    }

    @Override
    public void delete(String alias) throws DatabaseKeyStoreRepositoryException {
        this.keystoreCache.invalidate(alias);
        this.delegateRepository.delete(alias);

    }


    @Override
    public void store(DatabaseKeyStoreEntry databaseKeyStoreEntry)  throws DatabaseKeyStoreRepositoryException{

            this.keystoreCache.invalidate(databaseKeyStoreEntry.getLabel());
            this.delegateRepository.store(databaseKeyStoreEntry);
            this.keystoreCache.put(databaseKeyStoreEntry.getLabel(),databaseKeyStoreEntry);

    }
}
