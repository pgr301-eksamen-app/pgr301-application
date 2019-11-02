package no.kristiania.pgr301.exam.aop.logging.db;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogDbCallContext {

  @Around(value = "@annotation(logDbAnnotation)")
  public Object performLogging(final ProceedingJoinPoint joinPoint, final LogDbCall logDbAnnotation)
      throws Throwable {
    long timeStart = System.nanoTime();
    try {
      return joinPoint.proceed();
    } finally {

      String separator = ".";
      String tableName =
          thisOrThat(logDbAnnotation.tableName(), getTableNameFromCallingClass(joinPoint));
      String methodName =
          thisOrThat(logDbAnnotation.methodName(), joinPoint.getSignature().getName());

      log.info(
          "|DB_CALL|"
              + logDbAnnotation.type().name()
              + "|"
              + getDeltaTimeInMs(timeStart)
              + "|"
              + tableName
              + separator
              + methodName);
    }
  }

  private String thisOrThat(String expectedValue, String defaultValue) {
    if (expectedValue == null || expectedValue.trim().isEmpty()) {
      return defaultValue;
    }
    return expectedValue;
  }

  private String getTableNameFromCallingClass(ProceedingJoinPoint joinPoint) {
    try {
      String tableName = joinPoint.getSignature().getDeclaringType().getSimpleName();

      if (isNotNullOrEmpty(tableName) && tableName.toLowerCase().endsWith("repositoryparent")) {
        tableName =
            Arrays.stream(joinPoint.getTarget().getClass().getInterfaces())
                .filter(clazz -> clazz.getName().startsWith("no.kristiania"))
                .filter(clazz -> clazz.getAnnotation(Repository.class) != null)
                .map(Class::getSimpleName)
                .findFirst()
                .orElse(null);
      }

      if (isNotNullOrEmpty(tableName) && tableName.toLowerCase().endsWith("repository")) {
        return tableName.substring(0, tableName.toLowerCase().indexOf("repository"));
      }
      return tableName;
    } catch (Exception ex) {
      log.error("Could not find a table name from calling @LogDbCall", ex);
      return "Unknown";
    }
  }

  private boolean isNotNullOrEmpty(String value) {
    return value != null && !value.trim().isEmpty();
  }

  private String getDeltaTimeInMs(long startTime) {
    return String.valueOf((System.nanoTime() - startTime) / 1e6);
  }
}
