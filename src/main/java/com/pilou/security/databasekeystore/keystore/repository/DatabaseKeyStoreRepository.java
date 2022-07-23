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

package com.pilou.security.databasekeystore.keystore.repository;

import com.pilou.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;

import java.util.Enumeration;

public interface DatabaseKeyStoreRepository {
    DatabaseKeyStoreEntry getByAlias(String alias);

    int size();

    Enumeration<String> listAlias();

    void delete(String alias);

    void store(DatabaseKeyStoreEntry entry);
}
