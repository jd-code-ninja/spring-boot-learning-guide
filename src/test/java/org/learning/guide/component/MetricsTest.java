package org.learning.guide.component;

import org.learning.guide.schema.Metrics;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

public class MetricsTest extends BaseComponent {

  @Test
  public void getMetricsTest() {
    ResponseEntity<Metrics> responseEntity = testRestTemplate.getForEntity(getMetricsUrl(), Metrics.class);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody().getMem()).isGreaterThan(100);
  }

  @Test
  public void getCustomMetricsTest() throws InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.setCacheControl("no-cache");
    HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);

    ResponseEntity<Metrics> responseEntity = testRestTemplate.getForEntity(getMetricsUrl(), Metrics.class);

    Long numMetricRequests = responseEntity.getBody().getHelloWorldMetric() == null ? 0 : responseEntity.getBody().getHelloWorldMetric();
    testRestTemplate.exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);
    testRestTemplate.exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);
    testRestTemplate.exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);

    ResponseEntity<Metrics> latestMetricsResponseEntity = testRestTemplate.getForEntity(getMetricsUrl(), Metrics.class);

    assertThat(latestMetricsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(latestMetricsResponseEntity.getBody().getHelloWorldMetric()).isGreaterThan(numMetricRequests);
  }

  private String getMetricsUrl() {
    return urlFactoryForTesting.getMgmtUrl() + "/metrics";
  }

  private String getHelloWorldUrl() {
    return urlFactoryForTesting.getTestUrl() + "/hello";
  }
}
