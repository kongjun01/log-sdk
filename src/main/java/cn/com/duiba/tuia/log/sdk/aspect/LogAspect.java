package cn.com.duiba.tuia.log.sdk.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月20日 16:59
 * @descript:
 * @version: 1.0
 */
@Component
@Aspect
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(cn.com.duiba.tuia.log.sdk.annotation.Log)")
    public Object writeLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object resultVal = joinPoint.proceed();



        return resultVal;
    }
}
