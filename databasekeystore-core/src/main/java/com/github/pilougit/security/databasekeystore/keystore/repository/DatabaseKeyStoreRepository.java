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

import com.github.pilougit.security.databasekeystore.keystore.exceptions.DatabaseKeyStoreRepositoryException;
import com.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;

import java.util.Enumeration;

public interface DatabaseKeyStoreRepository< transaction extends DatabaseKeyStoreRepositoryTransaction> {

    transaction beginTransaction();

    void commitTransaction(transaction transaction);

    void rollBackTransaction(transaction transaction);

    DatabaseKeyStoreEntry getByAlias(String alias) throws DatabaseKeyStoreRepositoryException;

    int size() throws DatabaseKeyStoreRepositoryException;;

    Enumeration<String> listAlias() throws DatabaseKeyStoreRepositoryException;;

    void delete(String alias) throws DatabaseKeyStoreRepositoryException;;

    void store(DatabaseKeyStoreEntry entry) throws DatabaseKeyStoreRepositoryException;;

    public enum LOCKTYPE {
        LOCK,NOLOCK

    }
}
