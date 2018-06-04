package io.reflectoring.dsl;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "rootservice")
public interface RootClient {

  @RequestMapping(method = RequestMethod.POST, path = "/root")
  Root createRoot(@RequestBody Root root);

}
