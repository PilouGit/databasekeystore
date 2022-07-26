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

package com.github.pilougit.security.databasekeystore.keystore.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.pilougit.security.databasekeystore.keystore.exceptions.DatabaseKeyStoreEntrySerialization;
import com.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;

public class DatabaseKeyStoreEntrySerializationService {

    private  ObjectMapper objectMapper;

    public DatabaseKeyStoreEntrySerializationService()
    {
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());


    }
    public String serializeDatabaseKeyStoreEntry(DatabaseKeyStoreEntry entry) throws DatabaseKeyStoreEntrySerialization
    {
        try {
            return objectMapper.writeValueAsString(entry);
        } catch (JsonProcessingException e) {
            throw new DatabaseKeyStoreEntrySerialization(e);
        }
    }
    public DatabaseKeyStoreEntry unserializeDatabaseKeyStoreEntry(String databaseKeyStoreEntryString) throws DatabaseKeyStoreEntrySerialization
    {
        try {
            return objectMapper.readValue(databaseKeyStoreEntryString,DatabaseKeyStoreEntry.class);
        } catch (JsonProcessingException e) {
            throw new DatabaseKeyStoreEntrySerialization(e);
        }

    }
}
