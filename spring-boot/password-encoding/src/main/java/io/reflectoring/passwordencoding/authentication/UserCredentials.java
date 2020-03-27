package io.reflectoring.passwordencoding.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserCredentials {

  @Id private String username;

  private String password;

  boolean enabled;

  @ElementCollection
  @JoinTable(
      name = "authorities",
      joinColumns = {@JoinColumn(name = "username")})
  @Column(name = "authority")
  private Set<String> roles;
}
