package org.springframework.cloud.contract.verifier.tests;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.reflectoring.UserServiceBase;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;
import org.junit.Test;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;

public class UserserviceTest extends UserServiceBase {

	@Test
	public void validate_pactContract_0() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"firstName\":\"Arthur\",\"lastName\":\"Dent\"}");

		// when:
			ResponseOptions response = given().spec(request)
					.post("/user-service/users");

		// then:
			assertThat(response.statusCode()).isEqualTo(201);
			assertThat(response.header("Content-Type")).isEqualTo("application/json;charset=UTF-8");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
		// and:
			assertThat((Object) parsedJson.read("$.['id']")).isInstanceOf(java.lang.Integer.class);
	}

	@Test
	public void validate_pactContract_1() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"firstName\":\"Zaphod\",\"lastName\":\"Beeblebrox\"}");

		// when:
			ResponseOptions response = given().spec(request)
					.put("/user-service/users/42");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
		// and:
			assertThat((Object) parsedJson.read("$.['lastName']")).isInstanceOf(java.lang.String.class);
			assertThat((Object) parsedJson.read("$.['firstName']")).isInstanceOf(java.lang.String.class);
	}

	@Test
	public void validate_shouldSaveUser() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"firstName\":\"Arthur\",\"lastName\":\"Dent\"}");

		// when:
			ResponseOptions response = given().spec(request)
					.post("/user-service/users");

		// then:
			assertThat(response.statusCode()).isEqualTo(201);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['id']").isEqualTo(42);
	}

	@Test
	public void validate_shouldUpdateUser() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"firstName\":\"Arthur\",\"lastName\":\"Dent\"}");

		// when:
			ResponseOptions response = given().spec(request)
					.put("/user-service/users/42");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['id']").isEqualTo(42);
	}

}
