package it.redhat.demo.rest;

import it.redhat.demo.cache.CiaoCache;
import org.infinispan.Cache;
import org.infinispan.eviction.EvictionStrategy;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheRemoveAll;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fabio.ercoli@redhat.com on 26/12/16.
 */


@Path("cache")
public class CacheRestService {

    @Inject
    @CiaoCache
    private Cache<CacheKey, String> cache;

    @Path("name")
    @GET
    public String getCacheName() {
        return cache.getName();
    }

    @Path("size")
    @GET
    public int getNumberOfEntries() {
        return cache.size();
    }

    @Path("eviction/strategy")
    @GET
    public EvictionStrategy getEvictionStrategy() {
        return cache.getCacheConfiguration().eviction().strategy();
    }

    @Path("eviction/max")
    @GET
    public int getEvictionMaxEntries() {
        return cache.getCacheConfiguration().eviction().maxEntries();
    }

    @Path("values")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getCachedValues() {
        return cache.values();
    }

    @Path("entries")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getCachedEntries() {

        return cache.entrySet().stream().map(entry -> entry.toString()).collect(Collectors.toList());

    }

    @CacheRemoveAll(cacheName = "greeting-cache")
    @DELETE
    public void clearCache() {
    }

}
