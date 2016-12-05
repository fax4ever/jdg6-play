package it.redhat.demo.rest;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;
import java.util.stream.Collectors;

import static it.redhat.demo.rest.SmallRestService.CACHE_NAME;

/**
 * Created by fabio on 02/12/16.
 */

@Path("")
@Stateless
public class RestService {

    @Inject
    private EmbeddedCacheManager cacheManager;

    private Cache<String, String> cache;

    @PostConstruct
    private void init() {
        cache = cacheManager.getCache(CACHE_NAME);
    }

    @GET
    public String ciao() {
        return "ciao";
    }

    @Path("key/{key}")
    @GET
    public String get(@PathParam("key") String key) {

        return cache.get(key);

    }

    @Path("key/{key}")
    @PUT
    public void put(@PathParam("key") String key, String value) {

        cache.put(key, value);

    }

    @Path("key/{key}")
    @DELETE
    public void remove(@PathParam("key") String key) {

        cache.remove(key);

    }

    @Path("list/{max}")
    @GET
    public List<String> list(@PathParam("max") Integer max) {

        return cache.entrySet()
                .stream()
                .map(entry -> " { " + entry.getKey() + " : " + entry.getValue() + " } ")
                .limit(max)
                .collect(Collectors.toList());

    }



}
