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

package com.github.pilougit.security.databasekeystore.keystore.exceptions;

public class DatabaseKeyStoreRepositoryException extends Exception  {
    public DatabaseKeyStoreRepositoryException() {
        super();
    }

    public DatabaseKeyStoreRepositoryException(String message) {
        super(message);
    }

    public DatabaseKeyStoreRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseKeyStoreRepositoryException(Throwable cause) {
        super(cause);
    }

}