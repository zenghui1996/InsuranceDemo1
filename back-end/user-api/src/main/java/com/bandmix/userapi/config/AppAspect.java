package com.bandmix.userapi.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class AppAspect { 
 
	@Pointcut("execution(public * com.bandmix.userapi.controller.*.*(..))")
    public void controllerAspect() { 
    } 
	
	@Pointcut("execution(public * com.bandmix.userapi.service.*.*(..))")
    public void serviceAspect() { 
    } 
	
	@Pointcut("execution(public * com.bandmix.userapi.repository.*.*(..))")
    public void repositoryAspect() { 
    } 
 
    /**  
     * @param point 
     * @return 
     */ 
    @Around(value = "controllerAspect() || serviceAspect() || repositoryAspect()") 
    public Object doAround(ProceedingJoinPoint point) { 
    	
    	Object[] params = point.getArgs();
    	if(params.length != 0){
        	log.info("Request==> Methed : {}, Args : {}", point.getSignature().getName() , Arrays.toString(params));
    	}
    	
        Object result = null;
        try { 
        	result= point.proceed(); 

        } catch (Throwable throwable) { 
            throwable.printStackTrace(); 
            log.error(throwable.getMessage()); 
        }
        
        log.info("Response==> Methed : {}, Args : {}", point.getSignature().getName() , result); 
		return result; 

    } 
}