package io.reflectoring.beanlifecycle.jersey;

import io.reflectoring.beanlifecycle.ipdatabase.IpToLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Component
@Path("/api")
public class LocationResource {

    @Autowired
    HttpServletRequest servletRequest;

    @Autowired
    IpToLocationService locationService;

    @GET
    @Path("/location")
    public String location() {
        return locationService.getCountry(servletRequest.getRemoteAddr());
    }

    @POST
    @Path("/update")
    public void update() {
        locationService.updateIpDatabase();
    }

}