package it.redhat.demo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by fabio.ercoli@redhat.com on 26/12/16.
 */

@Path("ciao")
public class CiaoRestService {

    @GET
    public String ciao() {
        return "ciao";
    }

}
