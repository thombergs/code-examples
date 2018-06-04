package io.reflectoring.dsl;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslJsonRootValue;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        // overriding provider address
        "rootservice.ribbon.listOfServers: localhost:8888"
})
public class PactDslJsonBodyLikeMapperConsumerTest {

  @Rule
  public PactProviderRuleMk2 stubProvider = new PactProviderRuleMk2("testprovider", "localhost", 8888, this);

  @Autowired
  private RootClient rootClient;

  @Pact(state = "teststate", provider = "testprovider", consumer = "testclient")
  public RequestResponsePact createPact(PactDslWithProvider builder) {
    return builder
            .given("teststate")
            .uponReceiving("a POST request with a Root object")
            .path("/root")
            .method("POST")
//            .body(PactDslJsonBodyLikeMapper.like(new PactDslJsonBodyLikeMapperTest.Root()))
            .willRespondWith()
            .status(201)
            .matchHeader("Content-Type", "application/json")
            .body(PactDslJsonBodyLikeMapper.like(new Root()))
            .toPact();
  }

  @Pact(state = "teststate2", provider = "testprovider", consumer = "testclient")
  public RequestResponsePact createPact2(PactDslWithProvider builder) {
    return builder
            .given("teststate2")
            .uponReceiving("a POST request with a Root object")
            .path("/root")
            .method("POST")
            .willRespondWith()
            .status(201)
            .matchHeader("Content-Type", "application/json")
            .body(new PactDslJsonBody()
            .eachLike("arrayField", PactDslJsonRootValue.numberType()))
            .toPact();
  }

  @Test
  @PactVerification(fragment = "createPact")
  public void verifyPact() {
    rootClient.createRoot(new Root());
  }

  @Test
  @PactVerification(fragment = "createPact2")
  public void verifyPact2() {
    rootClient.createRoot(new Root());
  }

}
