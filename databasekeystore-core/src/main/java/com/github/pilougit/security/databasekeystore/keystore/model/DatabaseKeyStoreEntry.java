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

package com.github.pilougit.security.databasekeystore.keystore.model;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Data
public class DatabaseKeyStoreEntry {
    @NonNull
    protected String label;
    protected DatabaseKeyStoreProtectedKey cipherKey;
    protected DatabaseKeyStoreCertificate cert;
    protected List<DatabaseKeyStoreCertificate> chain;
    @NonNull
    protected LocalDate created;

    public boolean isCertEntry() {
        return cert != null;
    }

    public boolean isKeyEntry() {
        return cipherKey != null;
    }

}
