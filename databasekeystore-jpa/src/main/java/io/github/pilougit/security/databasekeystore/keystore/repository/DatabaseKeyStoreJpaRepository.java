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

import io.github.pilougit.security.databasekeystore.keystore.entity.DatabaseKeyStoreEntity;
import io.github.pilougit.security.databasekeystore.keystore.exceptions.DatabaseKeyStoreEntrySerialization;
import io.github.pilougit.security.databasekeystore.keystore.exceptions.DatabaseKeyStoreRepositoryException;
import io.github.pilougit.security.databasekeystore.keystore.model.DatabaseKeyStoreEntry;
import io.github.pilougit.security.databasekeystore.keystore.service.DatabaseKeyStoreEntrySerializationService;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

@Slf4j
@Data
public class DatabaseKeyStoreJpaRepository implements DatabaseKeyStoreRepository<DatabaseKeyStoreJpaRepositoryTransaction>{
    @NonNull
    protected EntityManager entityManager;
    protected DatabaseKeyStoreEntrySerializationService databaseKeyStoreEntrySerializationService=new DatabaseKeyStoreEntrySerializationService();


    @Override
    public DatabaseKeyStoreJpaRepositoryTransaction beginTransaction() {
        EntityTransaction transaction=entityManager.getTransaction();
        transaction.begin();
        return new DatabaseKeyStoreJpaRepositoryTransaction(transaction);
    }

    @Override
    public void commitTransaction(DatabaseKeyStoreJpaRepositoryTransaction transaction) {

      if (DatabaseKeyStoreJpaRepositoryTransaction.isClosable(transaction))  transaction.getTransaction().commit();
    }
    @Override
    public void rollBackTransaction(DatabaseKeyStoreJpaRepositoryTransaction transaction) {
        if (DatabaseKeyStoreJpaRepositoryTransaction.isClosable(transaction))  transaction.getTransaction().rollback();
    }
    @Override
    public DatabaseKeyStoreEntry getByAlias(String alias) throws DatabaseKeyStoreRepositoryException{
        DatabaseKeyStoreEntity entity=  findByAlias(alias);
        DatabaseKeyStoreEntry result=null;
        if (entity!=null)
        {
            try {
                result= databaseKeyStoreEntrySerializationService.unserializeDatabaseKeyStoreEntry(entity.getKeyStoreEntry());
            } catch (DatabaseKeyStoreEntrySerialization e) {
                throw new DatabaseKeyStoreRepositoryException(e);
            }
        }
        return result;
    }

    @Override
    public int size() {
        Query query = entityManager.createQuery("SELECT count(*) FROM DatabaseKeyStoreEntity d");
        Integer count=(Integer)query.getSingleResult();
        return count==null?0:count;
    }

    @Override
    public Enumeration<String> listAlias() {
        Query query = entityManager.createQuery("SELECT alias FROM DatabaseKeyStoreEntity d");
        List<String> listAlias=query.getResultList();



        return Collections.enumeration(listAlias);
    }

    @Override
    public void delete(String alias) {
        Query query = entityManager.createQuery("delete  FROM DatabaseKeyStoreEntity d where d.alias=:alias");
        query.setParameter("alias",alias);
        query.executeUpdate();

    }

    protected DatabaseKeyStoreEntity findByAlias(String alias)
    {
        DatabaseKeyStoreEntity entity=null;
     try{   Query query = entityManager.createQuery("FROM DatabaseKeyStoreEntity d where d.alias=:alias", DatabaseKeyStoreEntity.class);
        query.setParameter("alias",alias);
         entity= (DatabaseKeyStoreEntity) query.getSingleResult();
        return entity;
    } catch(NoResultException e) {

    }
        return entity;
    }
    @Override
    public void store(DatabaseKeyStoreEntry databaseKeyStoreEntry)  throws DatabaseKeyStoreRepositoryException{
        try {
            DatabaseKeyStoreEntity entity=  findByAlias(databaseKeyStoreEntry.getLabel());

        if (entity==null)
        {
             entity=new DatabaseKeyStoreEntity();
             entity.setAlias(databaseKeyStoreEntry.getLabel());
        }
           entity.setKeyStoreEntry(databaseKeyStoreEntrySerializationService.serializeDatabaseKeyStoreEntry(databaseKeyStoreEntry));
            entityManager.persist(entity);
        } catch (DatabaseKeyStoreEntrySerialization e) {
            throw new DatabaseKeyStoreRepositoryException(e);
        }
    }
}
