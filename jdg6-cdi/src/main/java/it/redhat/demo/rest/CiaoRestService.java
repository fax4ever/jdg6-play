package it.redhat.demo.rest;

import it.redhat.demo.service.CiaoService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Created by fabio.ercoli@redhat.com on 26/12/16.
 */

@Path("ciao")
public class CiaoRestService {

    @Inject
    private CiaoService service;

    @Path("{name}")
    @GET
    public String ciao(@PathParam("name") String name) {
        return service.ciao(name);
    }

}
