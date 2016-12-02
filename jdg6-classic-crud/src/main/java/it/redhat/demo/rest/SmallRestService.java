package it.redhat.demo.rest;

import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * @author Fabio Massimo Ercoli
 *         fabio.ercoli@redhat.com
 *         on 19/07/16
 */
@Stateless
@Path(SmallRestService.CACHE_NAME)
public class SmallRestService extends BaseRestService {

    public static final String CACHE_NAME = "small";

    @Inject
    private Logger log;

    @Inject
    private EmbeddedCacheManager cacheManager;

    @PostConstruct
    private void init() {

        cache = cacheManager.getCache(CACHE_NAME);

    }

}
