package pets.database.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.function.Predicate;

@Configuration
public class RequestLoggingFilterConfig {

  // ALSO SET LOGGER FOR org.springframework.web.filter.CommonsRequestLoggingFilter IN LOGBACK.XML
  @Bean
  public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
    CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setBeforeMessagePrefix("[REQUEST BEGIN] : ");
    filter.setIncludeClientInfo(true);
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(false); // can't mask passwords
    // filter.setMaxPayloadLength(10000);    // not needed because payload is not included
    filter.setIncludeHeaders(false);
    filter.setHeaderPredicate(getHeaderPredicate());
    filter.setAfterMessagePrefix("[REQUEST END] : ");
    return filter;
  }

  private Predicate<String> getHeaderPredicate() {
    return header -> !header.equals("authorization");
  }
}
