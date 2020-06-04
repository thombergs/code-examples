package io.reflectoring.codefirst;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootApplication
public class CodeFirstApplication {
    public static void main(String[] args) {
        SpringApplication.run(CodeFirstApplication.class, args);
    }
}

@RequestMapping("/api/todos")
@Tag(name = "Todo API", description = "euismod in pellentesque massa placerat duis ultricies lacus sed turpis")
@SecurityRequirement(name = "api")
interface TodoApi {

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    List<Todo> findAll();

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    Todo findById(@PathVariable String id);

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    Todo save(@RequestBody Todo todo);

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    Todo update(@PathVariable String id, @RequestBody Todo todo);

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void delete(@PathVariable String id);
}

@OpenAPIDefinition(
        info = @Info(
                title = "Code-First Approach (reflectoring.io)",
                description = "" +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur et rhoncus quam. Aenean quis augue ac eros pulvinar malesuada. " +
                        "In sagittis elit egestas tincidunt iaculis. " +
                        "Donec eu lacus vitae nulla varius consectetur a vel quam. Aliquam erat volutpat. Duis eget ullamcorper tellus",
                contact = @Contact(name = "Reflectoring", url = "https://reflectoring.io", email = "petros.stergioulas94@gmail.com"),
                license = @License(name = "MIT Licence", url = "https://github.com/thombergs/code-examples/blob/master/LICENSE")),
        servers = @Server(url = "http://localhost:8080")
)
@SecurityScheme(name = "api", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
class OpenAPIConfiguration {
}

@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/unsecured", "/swagger-ui/**", "/reflectoring-openapi/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("password"))
                .authorities("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Data
class Todo {
    private String id;
    private String text;
}

@RestController
class TodoController implements TodoApi {

    private List<Todo> todos;

    @Override
    public List<Todo> findAll() {
        return todos;
    }

    @Override
    public Todo findById(String id) {
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst().orElseGet(null);
    }

    @Override
    public Todo save(Todo todo) {
        todos.add(todo);
        return todo;
    }

    @Override
    public Todo update(String id, Todo todo) {
        for (Todo t : todos) {
            if (t.getId().equals(id)) {
                t.setText(todo.getText());
            }
        }

        return todo;
    }

    @Override
    public void delete(String id) {
        todos.removeIf(t -> t.getId().equals(id));
    }

    @PostConstruct
    public void onInit() {
        todos = Stream.of("Groceries", "Lisa's birthday")
                .map(todo -> new Todo(UUID.randomUUID().toString(), todo))
                .collect(Collectors.toList());
    }
}