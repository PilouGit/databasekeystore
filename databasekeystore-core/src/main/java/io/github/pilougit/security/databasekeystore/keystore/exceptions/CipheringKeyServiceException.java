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

package io.github.pilougit.security.databasekeystore.keystore.exceptions;

public class CipheringKeyServiceException extends Exception {
    public CipheringKeyServiceException() {
        super();
    }

    public CipheringKeyServiceException(String message) {
        super(message);
    }

    public CipheringKeyServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CipheringKeyServiceException(Throwable cause) {
        super(cause);
    }

    protected CipheringKeyServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

