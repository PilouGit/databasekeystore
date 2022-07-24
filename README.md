# databasekeystore
This package provides functionality to use KeyStore with support of multiple storage engine by implementing DatabaseKeyStoreRepository.
# Registring security provider.

To add the provider at runtime use:

```java
import java.security.Security;
import com.github.pilougit.security.databasekeystore.DatabaseKeyStoreProvider;
    
  Security.addProvider(new DatabaseKeyStoreProvider());

```
The provider can also be configured as part of your environment via static registration by adding an entry to the java.security properties file (found in $JAVA_HOME/jre/lib/security/java.security, where $JAVA_HOME is the location of your JDK/JRE distribution). You'll find detailed instructions in the file but basically it comes down to adding a line:
```java
security.provider.<n>=com.github.pilougit.security.databasekeystore.DatabaseKeyStoreProvider


```


# Supported Storage Engine


# Encryption schema

Private keys will be stored according to the implementation of CipheringKeyService which is either currently:
* noop ciphering engine NoOpCipheringKeyService
* AES/GCM engine AESGcmCipheringKeyService


