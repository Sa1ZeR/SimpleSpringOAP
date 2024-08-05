package com.sa1zer.botcamp7.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Точка среза, которая работает со всеми методами класса, которые помечены аннотацией @Service
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void logServiceMethod() {}

    /**
     * Точка среза, которая срабатывает на любой вызов метода
     */
    @Pointcut("execution(* *(*))")
    public void logServiceAfterReturnMethod() {}

    /**
     * Точка среза, которая срабатывает на любой вызов метода, который не возвращает никакого значения
     */
    @Pointcut("execution(void *(*))")
    public void logServiceAfterMethodExecution() {}

    /**
     * Совет, который срабатывает до вызова метода
     */
    @Before("logServiceMethod()")
    public void beforeMethodCall(JoinPoint joinPoint) {
        log.info("Method {} start execution with params: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    /**
     * Совет, который срабатывает после успешного завершения метода с возвращаемым значением
     */
    @AfterReturning(value = "logServiceAfterReturnMethod() && logServiceMethod()", returning = "result")
    public void afterReturnMethodCall(JoinPoint joinPoint, Object result) {
        log.info("Method {} successfully executed with returned value {}", joinPoint.getSignature().getName(), result);
    }

    /**
     * Совет, который срабатывает после успешного завершения void метода
     */
    @After("logServiceAfterMethodExecution() && logServiceMethod()")
    public void afterMethodCall(JoinPoint joinPoint) {
        log.info("Method {} successfully executed", joinPoint.getSignature().getName());
    }

    /**
     * Совет, который срабатывает после того, как в методе было брошено исключение
     */
    @AfterThrowing(value = "(logServiceAfterMethodExecution() || logServiceAfterReturnMethod()) && logServiceMethod()", throwing = "ex")
    public void afterMethodThrowingException(JoinPoint joinPoint, Exception ex) {
        log.info("Method {} thrown exception: {}", joinPoint.getSignature().getName(), ex.getMessage());
    }
}
