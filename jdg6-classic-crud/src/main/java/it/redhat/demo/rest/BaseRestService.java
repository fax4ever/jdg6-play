package it.redhat.demo.rest;

import org.infinispan.Cache;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabio Massimo Ercoli
 *         fabio.ercoli@redhat.com
 *         on 21/07/16
 */

public abstract class BaseRestService {

    protected Cache<String, String> cache;

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

    @Path("work")
    @DELETE
    public void work() {

        cache.clear();

    }

    @Path("work/{max}")
    @POST
    public void work(@PathParam("max") Integer max) {

        cache.clear();
        for (int i=0; i<max; i++) {
            cache.put("key" + i, "value" + i);
        }

    }

    @Path("workFail/{max}")
    @POST
    public void workFail(@PathParam("max") Integer max) {

        cache.clear();
        for (int i=0; i<max; i++) {
            cache.put("key" + i, "value" + i + "WRONG");
        }
        throw new RuntimeException();

    }

    @Path("batch/{max}")
    @POST
    public void batch(@PathParam("max") Integer max) {

        cache.startBatch();
        this.work(max);
        cache.endBatch(true);

    }

    @Path("batchFail/{max}")
    @POST
    public void batchFail(@PathParam("max") Integer max) {

        cache.startBatch();
        this.workFail(max);
        cache.endBatch(true);

    }

    @Path("list/{max}")
    @GET
    public List<String> list(@PathParam("max") Integer max) {
        ArrayList<String> result = new ArrayList<>();

        for (int i=0; i<max; i++) {
            String value = cache.get("key" + i);
            if (value != null) {
                result.add(value);
            }
        }

        return result;
    }

}
