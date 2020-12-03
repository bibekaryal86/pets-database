package pets.database.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import pets.database.service.GcpSecretManagerService;

import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static pets.database.utils.Constants.BASIC_AUTH_PWD;
import static pets.database.utils.Constants.BASIC_AUTH_USR;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private GcpSecretManagerService gcpSecretManagerService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers("/swagger-ui/")
                .and()
                .ignoring()
                .mvcMatchers(GET, "/tests/ping");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        Map<String, String> authConfig = gcpSecretManagerService.getAuthConfig();
        auth.inMemoryAuthentication()
                .withUser(authConfig.get(BASIC_AUTH_USR))
                .password("{noop}".concat(authConfig.get(BASIC_AUTH_PWD)))
                .roles("USER");
    }
}
