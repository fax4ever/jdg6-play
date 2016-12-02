package it.redhat.demo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by fabio on 02/12/16.
 */

@Path("")
public class RestService {

    @GET
    public String ciao() {
        return "ciao";
    }

}
