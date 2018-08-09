package io.reflectoring;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "userservice")
public interface UserClient {

  @RequestMapping(method = RequestMethod.GET, path = "/user-service/users/{id}")
  User getUser(@PathVariable("id") Long id);

  @RequestMapping(method = RequestMethod.PUT, path = "/user-service/users/{id}")
  User updateUser(@PathVariable("id") Long id, @RequestBody User user);

  @RequestMapping(method = RequestMethod.POST, path = "/user-service/users")
  IdObject createUser(@RequestBody User user);

}
