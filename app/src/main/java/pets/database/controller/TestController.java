package pets.database.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tests")
public class TestController {

  @CrossOrigin
  @GetMapping(value = "/ping", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> pingTest() {
    return new ResponseEntity<>("{\"ping\": \"successful\"}", OK);
  }
}
