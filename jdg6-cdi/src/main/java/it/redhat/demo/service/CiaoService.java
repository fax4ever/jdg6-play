package it.redhat.demo.service;

import javax.cache.annotation.CacheResult;

/**
 * Created by fabio.ercoli@redhat.com on 26/12/16.
 */
public class CiaoService {

    @CacheResult(cacheName = "ciao-cache")
    public String ciao(String name) {
        return "Ciao " + name + " :)";
    }

}
