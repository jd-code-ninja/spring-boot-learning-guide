package org.learning.guide.service;

import org.springframework.stereotype.Component;

@Component
public class PersonSummaryService {

  public PersonSummary getOrCalculate(String personId) {
    return new PersonSummary();
  }

  public void deletePersonSummary(String personId) {
    return;
  }
}
