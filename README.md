# databasekeystore
This package provides functionality to use [KeyStore](https://docs.oracle.com/javase/8/docs/api/java/security/KeyStore.html)  with support of multiple storage engine by implementing DatabaseKeyStoreRepository.

# Installation
The core package could be installed through maven with 

```xml 
<dependency>
  <groupId>io.github.pilougit.security</groupId>
  <artifactId>databasekeystore-core</artifactId>
  <version>1.0-alpha</version>
</dependency>
```


# Registring security provider.

To add the provider at runtime use:

```java
import java.security.Security;
import DatabaseKeyStoreProvider;
    
Security.addProvider(new DatabaseKeyStoreProvider());

```
The provider can also be configured as part of your environment via static registration by adding an entry to the java.security properties file (found in $JAVA_HOME/jre/lib/security/java.security, where $JAVA_HOME is the location of your JDK/JRE distribution). You'll find detailed instructions in the file but basically it comes down to adding a line:
```java
security.provider.<n>=DatabaseKeyStoreProvider


```


# Supported Storage Engine

* JPA Storage Engine
* Memory Storage Engine
* Cache Storage Engine 


# JPA Storage Engine

Could be installed through
```xml 
<dependency>
  <groupId>io.github.pilougit.security</groupId>
  <artifactId>databasekeystore-jpa</artifactId>
  <version>1.0-alpha</version>
</dependency>
```

The JPA Storage Engine pass by initialize a DatabaseKeyStoreJpaRepository with a entity manager:
```java
EntityManager em=...
KeyStore keystore = KeyStore.getInstance(DatabaseKeyStoreProvider.KEYSTORE, DatabaseKeyStoreProvider.PROVIDER_NAME);
keystore.load(new DatabaseKeyStoreLoadStoreParameter(new DatabaseKeyStoreJpaRepository(em), new AESGcmCipheringKeyService()));


```
##  DataBase schema for HSQLDB ##

Schema pre prepared for hsqldb database can be created by executing:

``` sql

create table keyentry(id bigint not null  GENERATED BY DEFAULT AS IDENTITY,alias varchar(255) not null, entry LONGVARCHAR not null,  primary key (id));

```
## DataBase schema for MySQL/MariaDB ## 

Schema pre prepared for hsqldb database can be created by executing:

``` sql
create table keyentry(
id bigint   AUTO_INCREMENT PRIMARY KEY,
alias varchar(255) not null,
entry LONGTEXT not null
);

CREATE UNIQUE INDEX idx_alias ON keyentry(alias);
```

## DataBase schema for PostgreSQL ## 

Schema pre prepared for hsqldb database can be created by executing:

``` sql
create table keyentry(
id bigserial PRIMARY KEY,
alias varchar(255) not null,
entry TEXT not null
);

CREATE UNIQUE INDEX idx_alias ON keyentry(alias);
```
# Memory Storage Engine
Could be installed through
```xml 
<dependency>
  <groupId>io.github.pilougit.security</groupId>
  <artifactId>databasekeystore-simple</artifactId>
  <version>1.0-alpha</version>
</dependency>
```

It is a simple Map to allow easy mock

# Cache Storage Engine
Could be installed through
```xml 
<dependency>
  <groupId>io.github.pilougit.security</groupId>
  <artifactId>databasekeystore-caffeine</artifactId>
  <version>1.0-alpha</version>
</dependency>
```

To achieve good performance it is possible to create a cache around the key storage engine.
The current implementation use caffeine to achieve it.

``` java
protected DatabaseKeyStoreRepository getDatabaseKeyStore()
{

        Cache<String, DatabaseKeyStoreEntry> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build();

        return new DatabaseKeyStoreCacheRepository(cache,new DatabaseKeyStoreMemoryRepository());
    }
    ...
 
 KeyStore keystore = KeyStore.getInstance(DatabaseKeyStoreProvider.KEYSTORE, DatabaseKeyStoreProvider.PROVIDER_NAME);
keystore.load(new DatabaseKeyStoreLoadStoreParameter(getDatabaseKeyStore(), new AESGcmCipheringKeyService()));

```
# Encryption schema

Private keys will be stored according to the implementation of CipheringKeyService which is either currently:
* noop ciphering engine NoOpCipheringKeyService
* AES/GCM engine AESGcmCipheringKeyService


