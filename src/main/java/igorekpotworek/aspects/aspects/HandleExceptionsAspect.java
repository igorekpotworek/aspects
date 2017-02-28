package igorekpotworek.aspects.aspects;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import igorekpotworek.aspects.annotations.Retry;
import lombok.extern.java.Log;

@Aspect
@Component
@Log
public class HandleExceptionsAspect {

	@Around("@annotation(igorekpotworek.aspects.annotations.Retry)")
	public void handleException(ProceedingJoinPoint joinPoint) throws Throwable {

		int retryCount = 0;
		Retry annotation = getRetryAnnotation(joinPoint);

		while (retryCount < annotation.attempts()) {
			try {
				joinPoint.proceed();
			} catch (Throwable e) {
				handleException(e, annotation.exceptionClass(), annotation.timeout());
				++retryCount;
			}
		}

		joinPoint.proceed();
	}

	@SuppressWarnings("rawtypes")
	private void handleException(Throwable e, Class exceptionClass, long timeout) throws Throwable {
		if (exceptionClass.isInstance(e)) {
			log.warning("Exception: " + e.getMessage());
			Thread.sleep(timeout);
			log.info("Retrying...");
		} else
			throw e;
	}

	private Retry getRetryAnnotation(ProceedingJoinPoint joinPoint) {
		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
		Method method = ms.getMethod();
		return method.getAnnotation(Retry.class);
	}
}
