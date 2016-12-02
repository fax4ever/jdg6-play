package it.redhat.demo.producer;

import it.redhat.demo.rest.LargeRestService;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * @author Fabio Massimo Ercoli
 *         fabio.ercoli@redhat.com
 *         on 19/07/16
 */

@Singleton
@Startup
public class CacheManagerProducer {

    private static final String LOY_DATAGRID_MAXENTRIES = "loy.datagrid.maxentries";
    private static final String LOY_DATAGRID_PASSIVESTORAGE = "loy.datagrid.passivestorage";
    private static final String LOY_DATAGRID_PURGEONSTARTUP = "loy.datagrid.purgeonstartup";

    @Inject
    private Logger log;

    private EmbeddedCacheManager cacheManager;

    @PostConstruct
    private void init() {

        Integer maxentries = Integer.parseInt(System.getProperty(LOY_DATAGRID_MAXENTRIES, "200000"));
        String passivestorage = System.getProperty(LOY_DATAGRID_PASSIVESTORAGE, "loystorage");
        boolean purgeonstartup = Boolean.parseBoolean(System.getProperty(LOY_DATAGRID_PURGEONSTARTUP, "true"));

        GlobalConfiguration globalConfiguration = new GlobalConfigurationBuilder()
            .transport()
                .defaultTransport()
                .clusterName("eap6-jdg-lib-repl")
                .distributedSyncTimeout(600000l)
                .addProperty("configurationFile","jgroups-udp.xml")
            .globalJmxStatistics()
                .allowDuplicateDomains(true)
                .enable()
            .build();

        Configuration transactional = new ConfigurationBuilder()
            .transaction()
                .transactionMode(TransactionMode.TRANSACTIONAL)
                .lockingMode(LockingMode.OPTIMISTIC)
                .transactionManagerLookup(new GenericTransactionManagerLookup())
                .syncCommitPhase(true)
                .useSynchronization(false)
            .locking()
                .isolationLevel(IsolationLevel.READ_COMMITTED)
                .concurrencyLevel(1000)
                .useLockStriping(false)
            .invocationBatching()
                .enable(true)
        .build();

        Configuration small = new ConfigurationBuilder()
            .read(transactional)
            .clustering()
                .cacheMode(CacheMode.REPL_ASYNC)
                .stateTransfer()
                    .chunkSize(16384)
        .build();

        Configuration large = new ConfigurationBuilder()
            .read(transactional)
            .clustering()
                .cacheMode(CacheMode.DIST_ASYNC)
                .hash()
                    .numOwners(2)
                .stateTransfer()
                    .chunkSize(16384)
            .persistence()
                .passivation(true)
                .addSingleFileStore()
                .location(passivestorage)
                .preload(false)
                .purgeOnStartup(purgeonstartup)
                .shared(false)
                .singleton()
            .eviction()
                .maxEntries(maxentries)
        .build();

        cacheManager = new DefaultCacheManager(globalConfiguration, small);
        cacheManager.defineConfiguration(LargeRestService.CACHE_NAME, large);

        cacheManager.start();

    }

    @PreDestroy
    public void disposeCacheManager() {

        log.info("Stop embedded cache manager");
        cacheManager.stop();

    }

    @Produces
    public EmbeddedCacheManager getCacheManager() {
        return cacheManager;
    }

}
