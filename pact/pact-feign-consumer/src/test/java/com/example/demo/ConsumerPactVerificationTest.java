package com.example.demo;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.apache.http.entity.ContentType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        // overriding provider address
        "addresses.ribbon.listOfServers: localhost:8888"
})
public class ConsumerPactVerificationTest {

  @Rule
  public PactProviderRuleMk2 stubProvider = new PactProviderRuleMk2("customerServiceProvider", "localhost", 8888, this);

  @Autowired
  private AddressClient addressClient;

  @Pact(state = "a collection of 2 addresses", provider = "customerServiceProvider", consumer = "addressClient")
  public RequestResponsePact createAddressCollectionResourcePact(PactDslWithProvider builder) {
    return builder
            .given("a collection of 2 addresses")
            .uponReceiving("a request to the address collection resource")
            .path("/addresses/")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body("{\n" +
                    "  \"_embedded\": {\n" +
                    "    \"addresses\": [\n" +
                    "      {\n" +
                    "        \"street\": \"Elm Street\",\n" +
                    "        \"_links\": {\n" +
                    "          \"self\": {\n" +
                    "            \"href\": \"http://localhost:8080/addresses/1\"\n" +
                    "          },\n" +
                    "          \"address\": {\n" +
                    "            \"href\": \"http://localhost:8080/addresses/1\"\n" +
                    "          },\n" +
                    "          \"customer\": {\n" +
                    "            \"href\": \"http://localhost:8080/addresses/1/customer\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"street\": \"High Street\",\n" +
                    "        \"_links\": {\n" +
                    "          \"self\": {\n" +
                    "            \"href\": \"http://localhost:8080/addresses/2\"\n" +
                    "          },\n" +
                    "          \"address\": {\n" +
                    "            \"href\": \"http://localhost:8080/addresses/2\"\n" +
                    "          },\n" +
                    "          \"customer\": {\n" +
                    "            \"href\": \"http://localhost:8080/addresses/2/customer\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"_links\": {\n" +
                    "    \"self\": {\n" +
                    "      \"href\": \"http://localhost:8080/addresses{?page,size,sort}\",\n" +
                    "      \"templated\": true\n" +
                    "    },\n" +
                    "    \"profile\": {\n" +
                    "      \"href\": \"http://localhost:8080/profile/addresses\"\n" +
                    "    },\n" +
                    "    \"search\": {\n" +
                    "      \"href\": \"http://localhost:8080/addresses/search\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"page\": {\n" +
                    "    \"size\": 20,\n" +
                    "    \"totalElements\": 2,\n" +
                    "    \"totalPages\": 1,\n" +
                    "    \"number\": 0\n" +
                    "  }\n" +
                    "}", "application/hal+json")
            .toPact();
  }

  @Pact(state = "a single address", provider = "customerServiceProvider", consumer = "addressClient")
  public RequestResponsePact createAddressResourcePact(PactDslWithProvider builder) {
    return builder
            .given("a single address")
            .uponReceiving("a request to the address resource")
            .path("/addresses/1")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body("{\n" +
                    "  \"street\": \"Elm Street\",\n" +
                    "  \"_links\": {\n" +
                    "    \"self\": {\n" +
                    "      \"href\": \"http://localhost:8080/addresses/1\"\n" +
                    "    },\n" +
                    "    \"address\": {\n" +
                    "      \"href\": \"http://localhost:8080/addresses/1\"\n" +
                    "    },\n" +
                    "    \"customer\": {\n" +
                    "      \"href\": \"http://localhost:8080/addresses/1/customer\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}", "application/hal+json")
            .toPact();
  }

  @Test
  @PactVerification(fragment = "createAddressCollectionResourcePact")
  public void verifyAddressCollectionPact() {
    Resources<Address> addresses = addressClient.getAddresses();
    assertThat(addresses).hasSize(2);
  }

  @Test
  @PactVerification(fragment = "createAddressResourcePact")
  public void verifyAddressPact() {
    Resource<Address> address = addressClient.getAddress(1L);
    assertThat(address).isNotNull();
  }

}
