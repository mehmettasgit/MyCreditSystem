package com.creditsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogginAspect {

    @Pointcut("execution(* com.creditsystem.service.*.*(..))")
    public void serviceMethods(){}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint){
        log.info("Başladı: {} parametrelerle: {}", joinPoint.getSignature(), joinPoint.getArgs());

    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Tamamlandı: {} dönüş: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("HATA oluştu: {} mesaj: {}", joinPoint.getSignature(), ex.getMessage());
    }
}
