package io.reflectoring.passwordencoding.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reflectoring.passwordencoding.authentication.UserCredentials;
import io.reflectoring.passwordencoding.authentication.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarResourcesTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private UserRepository userRepository;

  @Test
  void getCarsShouldReturnUnauthorizedIfTheRequestHasNoBasicAuthentication() throws Exception {
    mockMvc.perform(get("/cars")).andExpect(status().isUnauthorized());
  }

  @Test
  void getCarsShouldReturnCarsForTheAuthenticatedUser() throws Exception {
    mockMvc.perform(get("/cars").with(httpBasic("user", "password"))).andExpect(status().isOk());
  }

  @Test
  void registrationShouldReturnCreated() throws Exception {

    // register
    UserCredentialsDto userCredentialsDto =
        UserCredentialsDto.builder().username("toyota").password("my secret").build();
    mockMvc
        .perform(
            post("/registration")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userCredentialsDto)))
        .andExpect(status().isCreated());
  }

  @Test
  void registrationShouldReturnUnauthorizedWithWrongCredentials() throws Exception {

    mockMvc
        .perform(get("/cars").with(httpBasic("user", "wrong password")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void getCarsShouldUpdatePasswordFromWorkingFactor5toHigherValue() throws Exception {
    mockMvc
        .perform(get("/cars").with(httpBasic("user with working factor 5", "password")))
        .andExpect(status().isOk());

    UserCredentials userCredentials = userRepository.findByUsername("user with working factor 5");
    // we don't know what strength the BcCryptWorkFactorService returns,
    // but it should be more than 5
    assertThat(userCredentials.getPassword()).doesNotStartWith("{bcrypt}$2a$05");
  }

  @Test
  void getCarsShouldMigrateSha1PasswordToBcrypt() throws Exception {
    mockMvc
        .perform(get("/cars").with(httpBasic("user with sha1 encoding", "password")))
        .andExpect(status().isOk());

    UserCredentials userCredentials = userRepository.findByUsername("user with sha1 encoding");
    assertThat(userCredentials.getPassword()).startsWith("{bcrypt}");
  }

  @Test
  void getCarsShouldReturnOkForScryptUser() throws Exception {
    mockMvc
        .perform(get("/cars").with(httpBasic("scrypt user", "password")))
        .andExpect(status().isOk());
  }
}
