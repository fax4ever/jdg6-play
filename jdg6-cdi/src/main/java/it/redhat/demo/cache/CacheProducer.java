package it.redhat.demo.cache;

import org.infinispan.cdi.ConfigureCache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Created by fabio.ercoli@redhat.com on 26/12/16.
 */
@Singleton
public class CacheProducer {

    @CiaoCache
    @ConfigureCache("ciao-cache")
    @Produces
    public Configuration ciaoCache() {
        return new ConfigurationBuilder()
                .eviction().strategy(EvictionStrategy.LRU).maxEntries(4)
                .build();
    }

    @Produces
    @ApplicationScoped
    public EmbeddedCacheManager greetingCacheManager(@CiaoCache Configuration configuration) {
        GlobalConfiguration globalConfiguration = new GlobalConfigurationBuilder().globalJmxStatistics().allowDuplicateDomains(true).build();
        return new DefaultCacheManager(globalConfiguration, configuration);
    }

}
