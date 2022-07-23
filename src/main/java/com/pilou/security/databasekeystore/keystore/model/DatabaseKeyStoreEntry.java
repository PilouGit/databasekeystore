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

package com.pilou.security.databasekeystore.keystore.model;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

@Slf4j
@Data
public class DatabaseKeyStoreEntry {
    @NonNull
    protected String label;
    protected String cipherKey;
    protected String cert;
    protected String chain;
    protected String keyPassword;
    @NonNull
    protected LocalDate created;

    public boolean isCertEntry()
    {
        return StringUtils.isNoneBlank(cert);
    }
    public boolean isKeyEntry()
    {
        return StringUtils.isNoneBlank(cipherKey);
    }

}
