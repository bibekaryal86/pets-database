package pets.database.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
public class SwaggerConfig {

  private static final String AUTH_TYPE = "basicAuth";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    AUTH_TYPE, new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
        .security(Collections.singletonList(new SecurityRequirement().addList(AUTH_TYPE)))
        .info(
            new Info()
                .title("Database Layer fo PETS App")
                .description("Database Layer for PETS App for MongoDB Repository")
                .contact(new Contact().name("Bibek Aryal"))
                .license(new License().name("Personal Use Only"))
                .version("1.0.1"));
  }

  @Controller
  public static class SwaggerRedirectControllerDevelopment {
    @GetMapping("/")
    public String redirectToSwagger() {
      return "redirect:/swagger-ui/index.html";
    }
  }
}
