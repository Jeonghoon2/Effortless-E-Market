package com.effortless.effortlessmarket.global.aspect;

import com.effortless.effortlessmarket.global.component.LoggerComponent;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 100)
@Component
public class RequestAspect {

    private LoggerComponent logger;

    public RequestAspect(LoggerComponent logger) {
        this.logger = logger;
    }

    /**
     * Allow Annotation List
     */
    @Pointcut(
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
                    "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
    )
    public void allowAnnotations() {
    }

    /**
     * Allow Package List
     */
    @Pointcut("within(com.effortless.effortlessmarket..*)")
    public void allowPackages() {
    }

    @Pointcut("!@annotation(com.effortless.effortlessmarket.global.annotaion.ExcludeLogging)")
    public void excludeLogging() {
    }

    @Around("allowAnnotations() && allowPackages() && excludeLogging()")
    public Object aroundRequestMapping(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return logger.aroundRequestMapping(request, joinPoint);
    }


}
