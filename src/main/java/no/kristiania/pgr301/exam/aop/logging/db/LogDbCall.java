package no.kristiania.pgr301.exam.aop.logging.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogDbCall {
  Type type();

  String tableName() default "";

  String methodName() default "";

  enum Type {
    INSERT,
    SELECT,
    UPDATE,
    DELETE
  }
}
