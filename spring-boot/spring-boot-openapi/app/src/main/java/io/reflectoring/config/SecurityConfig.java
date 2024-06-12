package io.reflectoring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security 配置
 *
 * @author liujun
 * @date 2021-01-05 17:55:10
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // eureka开启security后，加解密接口encrypt访问不到,需要禁用csrf，客户端才能正常注册
        http.csrf().disable();
        // 支持httpBasic
        http.authorizeRequests()
                // permit /actuator/**以解决spring boot admin监控不到的问题
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/sms/**").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest()
                .authenticated().and().httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // 代码省略...

    }
}