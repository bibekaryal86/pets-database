package pets.database.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static pets.database.utils.Constants.BASIC_AUTH_PWD;
import static pets.database.utils.Constants.BASIC_AUTH_USR;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

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
    public void configure(WebSecurity webSecurity) {
        webSecurity
                .ignoring()
                .antMatchers("/swagger-ui/")
                .and()
                .ignoring()
                .mvcMatchers(GET, "/tests/ping");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        Map<String, String> authConfig = getAuthConfig();
        auth.inMemoryAuthentication()
                .withUser(authConfig.get(BASIC_AUTH_USR))
                .password("{noop}".concat(authConfig.get(BASIC_AUTH_PWD)))
                .roles("USER");
    }

    private Map<String, String> getAuthConfig() {
        Map<String, String> authConfigMap = new HashMap<>();

        if (System.getProperty(BASIC_AUTH_USR) != null) {
            // for running locally
            authConfigMap.put(BASIC_AUTH_USR, System.getProperty(BASIC_AUTH_USR));
            authConfigMap.put(BASIC_AUTH_PWD, System.getProperty(BASIC_AUTH_PWD));
        } else if (System.getenv(BASIC_AUTH_USR) != null) {
            // for GCP
            authConfigMap.put(BASIC_AUTH_USR, System.getenv(BASIC_AUTH_USR));
            authConfigMap.put(BASIC_AUTH_PWD, System.getenv(BASIC_AUTH_PWD));
        }

        return authConfigMap;
    }
}
