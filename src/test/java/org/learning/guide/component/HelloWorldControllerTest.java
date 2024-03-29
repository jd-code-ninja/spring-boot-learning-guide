package org.learning.guide.component;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldControllerTest extends BaseComponent {

  @Test
  public void getHelloWorld() {
    ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/hello", String.class);

    assertThat(responseEntity.getStatusCode())
        .as("Make sure you have the @RestController configured properly")
        .isEqualTo(HttpStatus.OK);
  }

  @Test
  public void getHelloWorldVerifyString() {
    ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/hello", String.class);

    assertThat(responseEntity.getStatusCode())
        .as("Make sure you have the @RestController configured properly")
        .isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody())
        .as("Make sure are returning the proper value 'Hello!'")
        .contains("Hello!");
  }
}
