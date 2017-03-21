package cn.com.duiba.tuia.log.sdk.aspect;

import cn.com.duiba.tuia.log.sdk.annotation.Log;
import cn.com.duiba.tuia.log.sdk.cache.CacheKey;
import cn.com.duiba.tuia.log.sdk.cache.ThreadLocalCache;
import cn.com.duiba.tuia.log.sdk.dto.LogDTO;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月20日 16:59
 * @descript:
 * @version: 1.0
 */
@Order(1)
@Aspect
public class LogAspect{

    private final String OPTION_JOIN_STR = ":";

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired(required = false)
    private HttpServletRequest request;

    @Pointcut("@annotation(cn.com.duiba.tuia.log.sdk.annotation.Log)")
    public void doing() {}


    @Before("doing()")
    public void doBefore(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Log log = method.getAnnotation(Log.class);

        LogDTO logDTO = this.setLogDTO(log);
        this.setOpeartionContent(logDTO,joinPoint.getArgs());
        this.setFirstName(method);
        this.initRequest(logDTO);
    }

    @AfterReturning(returning = "result", pointcut = "doing()")
    public void doAfterReturning(Object result) throws Throwable {

    }

    @AfterThrowing(pointcut = "doing()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        logger.error("写日志异常",throwable);
    }

    /**
     * 设置请求参数,只在第一次
     * @param logDTO
     */
    private void setOpeartionContent(LogDTO logDTO,Object[] args){

        if(args == null || ThreadLocalCache.isKeyExist(CacheKey.FIRST_CLASS)){
            return;
        }

        logDTO.setOpeartionContent(JSON.toJSONString(args));
    }

    /**
     * 初始化url和ip,如果不是web请求直接忽略
     * @param logDTO
     */
    private void initRequest(LogDTO logDTO){

        //如果不是request请求直接忽略
        if(request == null || ThreadLocalCache.isKeyExist(CacheKey.FIRST_CLASS)){
            return;
        }

        logDTO.setUri(request.getRequestURI());
        logDTO.setIp(getIpAddr(request));
    }

    /**
     * 设置第一个类
     * <p>用来判断是否闭环</p>
     * @param method
     */
    private void setFirstName(Method method){

        String className = method.getClass().toString();
        if(!ThreadLocalCache.isKeyExist(className)){
            ThreadLocalCache.put(CacheKey.FIRST_CLASS,className);
        }
    }

    /**
     * 设置日志DTO
     * @param log
     * @return
     */
    private LogDTO setLogDTO(Log log) {

        LogDTO logDTO = ThreadLocalCache.isKeyExist(CacheKey.LOG_KEY) ? (LogDTO) ThreadLocalCache.get(CacheKey.LOG_KEY) : new LogDTO();

        if (StringUtils.hasLength(log.moduleName()) && StringUtils.isEmpty(logDTO.getModuleName())) {
            logDTO.setModuleName(log.moduleName());
        }

        if (log.platform() != null && StringUtils.isEmpty(logDTO.getPlatform())) {
            logDTO.setPlatform(log.platform().toString());
        }

        if (StringUtils.hasLength(log.subModuleName()) && StringUtils.isEmpty(logDTO.getSubModuleName())) {
            logDTO.setSubModuleName(log.subModuleName());
        }

        if (logDTO.getTimestamp() == null) {
            logDTO.setTimestamp(new Date());
        }

        if(StringUtils.hasLength(log.optionName())){
            StringBuffer logOption = new StringBuffer();
            logOption.append(logDTO.getOptionName()).append(OPTION_JOIN_STR).append(log.optionName());
            logDTO.setOptionName(logOption.toString());
        }

        ThreadLocalCache.put(CacheKey.LOG_KEY,logDTO);

        return logDTO;
    }

    /**
     * 获取登录用户远程主机ip地址
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
