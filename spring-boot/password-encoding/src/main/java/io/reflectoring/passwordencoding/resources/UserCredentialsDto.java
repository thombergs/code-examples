package io.reflectoring.passwordencoding.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class UserCredentialsDto {
  private String username;
  private String password;
}
