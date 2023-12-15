package com.lhn.gps.config;


import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * com.lhn.gps..*Controller.*(..))")
    public void webLog(){

    }

    @Before("webLog()")
    public void doBefore(){

    }

    @AfterReturning(value = "webLog()",returning = "ret")
    public void doAfterReturning(Object ret){

    }


    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info(JSONUtil.toJsonStr(request));
        String urlStr = request.getRequestURI().toString();
        log.info(urlStr);
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        log.info(JSONUtil.toJsonStr(method));
        Object result = point.proceed();
        log.info(JSONUtil.toJsonStr(result));
        return result;
    }
}
