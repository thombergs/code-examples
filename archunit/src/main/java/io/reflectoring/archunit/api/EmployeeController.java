package io.reflectoring.archunit.api;

import io.reflectoring.archunit.service.EmployeeResponse;
import io.reflectoring.archunit.service.EmployeeService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class EmployeeController {

    @GET()
    @Path("/employees")
    public EmployeeResponse getEmployee() {
        EmployeeService service = new EmployeeService();
        return service.getEmployee();
    }

    // Uncomment to cause ArchUnit rule violation
//    @GET()
//    @Path("/employees")
//    public EmployeeResponse getEmployee_withViolation() {
//        EmployeeDao dao = new EmployeeDao();
//        Employee employee = dao.findEmployee();
//        return new EmployeeResponse(
//            employee.id(),
//            employee.name(),
//            employee.active()
//        );
//    }
}
