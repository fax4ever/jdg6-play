package it.redhat.demo.rest;

import org.infinispan.manager.EmbeddedCacheManager;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * Created by fabio on 05/12/16.
 */
@Stateless
@Path(SiteRestService.CACHE_NAME)
public class SiteRestService extends BaseRestService{

    public static final String CACHE_NAME = "site";

    @Inject
    private EmbeddedCacheManager cacheManager;

    @PostConstruct
    private void init() {

        cache = cacheManager.getCache(CACHE_NAME);

    }

}
