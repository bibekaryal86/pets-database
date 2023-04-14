package pets.database.config;

import static pets.database.utils.Constants.BASIC_AUTH_PWD;
import static pets.database.utils.Constants.BASIC_AUTH_USR;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeHttpRequests()
        .anyRequest()
        .authenticated()
        .and()
        .csrf()
        .disable()
        .httpBasic()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return httpSecurity.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->
        web.ignoring()
            .requestMatchers(HttpMethod.GET, "/tests/ping")
            .and()
            .ignoring()
            .requestMatchers(HttpMethod.GET, "/swagger-ui/");
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    Map<String, String> authConfig = getAuthConfig();
    UserDetails user =
        User.withDefaultPasswordEncoder()
            .username(authConfig.get(BASIC_AUTH_USR))
            .password("{noop}".concat(authConfig.get(BASIC_AUTH_PWD)))
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
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
