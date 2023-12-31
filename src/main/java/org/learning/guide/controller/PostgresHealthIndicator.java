package org.learning.guide.controller;

import org.springframework.beans.factory.annotation.Value;

public class PostgresHealthIndicator {

  private final String dynamoDbTableName;

  public PostgresHealthIndicator(@Value("${DYNAMODB_TABLE_NAME}") String dynamoDbTableName) {
    this.dynamoDbTableName = dynamoDbTableName;
  }
}
