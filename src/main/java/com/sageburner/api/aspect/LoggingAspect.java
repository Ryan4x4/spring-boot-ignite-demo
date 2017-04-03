package com.sageburner.api.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

/**
 * Created by Ryan Holt on 10/28/2015.
 */
@Aspect
public class LoggingAspect {

    private final Log logger = LogFactory.getLog(getClass());

    // FIXME get Aspect logging working
    @Around("execution(* com.sageburner.api..*.*(..))")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object retVal = joinPoint.proceed();

        stopWatch.stop();

        StringBuffer logMessage = new StringBuffer();
        logMessage.append(joinPoint.getTarget().getClass().getName());
        logMessage.append(".");
        logMessage.append(joinPoint.getSignature().getName());
        logMessage.append("(");
        // append args
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            logMessage.append(args[i]).append(",");
        }
        if (args.length > 0) {
            logMessage.deleteCharAt(logMessage.length() - 1);
        }

        logMessage.append(")");
        logMessage.append(" execution time: ");
        logMessage.append(stopWatch.getTotalTimeMillis());
        logMessage.append(" ms");
        logger.info(logMessage.toString());
        return retVal;
    }

    @Around("execution(* com.sageburner.api.creditcard.dao..*.*(..))")
    public Object logDaoExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuffer joinPointDetails = new StringBuffer();
        joinPointDetails.append(joinPoint.getTarget().getClass().getName());
        joinPointDetails.append(".");
        joinPointDetails.append(joinPoint.getSignature().getName());
        joinPointDetails.append("(");
        // append args
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            joinPointDetails.append(args[i]).append(",");
        }
        if (args.length > 0) {
            joinPointDetails.deleteCharAt(joinPointDetails.length() - 1);
        }

        joinPointDetails.append(")");

        logger.info("START: Executing DAO method: " + joinPointDetails.toString());

        Object retVal = joinPoint.proceed();

        logger.info("End: Executing DAO method: " + joinPointDetails.toString());

        return retVal;
    }
}
