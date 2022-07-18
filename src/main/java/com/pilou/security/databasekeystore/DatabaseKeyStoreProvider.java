package com.pilou.security.databasekeystore;

import lombok.extern.slf4j.Slf4j;

import java.security.Provider;
import java.util.List;

@Slf4j
public class DatabaseKeyStoreProvider extends Provider {

    protected static final String PROVIDER_NAME="DatabaseKeyStoreProvider";
    protected static final double version=1.0d;
    protected static final String PROVIDER_INFO="Information";


    public DatabaseKeyStoreProvider() {
        super(PROVIDER_NAME, version, PROVIDER_INFO);
        DatabaseKeyStoreProvider.this.putService(new DatabaseKeyStoreService(this, "KeyStore",
                "DatabaseKeyStoreProvider", "com.pilou.security.databasekeystore.DatabaseKeyStoreService", (List)null, null));

    }


}
