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

package io.github.pilougit.security.databasekeystore.keystore.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DatabaseKeyStoreProtectedKey {
    @NonNull
    @JsonProperty("saltKey")
    protected String saltKey;
    @NonNull
    @JsonProperty("ivCiphedKey")
    protected String ivCiphedKey;
    @NonNull
    @JsonProperty("saltCipher")
    protected String saltCipher;
    @NonNull
    @JsonProperty("cipheredData")
    protected String cipheredData;
    @NonNull
    @JsonProperty("algorithmCipheredData")
    protected String algorithmCipheredData;
    @NonNull
    @JsonProperty("typeCipheredKey")
    protected TypeCipheredKey typeCipheredKey;
}
