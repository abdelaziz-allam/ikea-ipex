package com.ikea.product.catalog.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

import static com.ikea.product.catalog.constant.AppConstants.*;

@Aspect
@Component
@Slf4j
public class MethodLoggerAspect {

  @Pointcut("execution(* com.ikea.*.*.service.*.*.*(..))")
  public void allServicesMethods() {
    // Will log all methods' entry and exit
  }

  @Before("allServicesMethods()")
  public void logBeforeMethodCall(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getName();
    Object[] args = joinPoint.getArgs();

    log.info(METHOD_LOG_BEFORE_METHOD_CALL, className, methodName);
    log.info(
        METHOD_LOG_PARAMETER_USED, className, methodName, argsToString(args));
  }

  @After("allServicesMethods()")
  public void logAfterMethodCall(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getName();

    log.info(METHOD_LOG_AFTER_METHOD_CALL, className, methodName);
  }

  private String argsToString(Object... args) {
    StringJoiner result = new StringJoiner(", ", "[", "]");

    for (Object arg : args) {
      result.add(String.valueOf(arg));
    }

    return result.toString();
  }
}
