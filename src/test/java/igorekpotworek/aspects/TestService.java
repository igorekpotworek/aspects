package igorekpotworek.aspects;

import java.io.IOException;

import org.springframework.stereotype.Service;

import igorekpotworek.aspects.annotations.Retry;

@Service
public class TestService {

	@Retry(attempts = RetryAspectTest.ATTEMPTS, timeout = RetryAspectTest.TIMEOUT, exceptionClass = IOException.class)
	public void testMethodWithExceptionThatShouldRetry() throws Exception {
		throw RetryAspectTest.EXCEPTION;
	}

	@Retry(attempts = RetryAspectTest.ATTEMPTS, timeout = RetryAspectTest.TIMEOUT, exceptionClass = NullPointerException.class)
	public void testMethodWithExceptionThatShouldntRetry() throws Exception {
		throw RetryAspectTest.EXCEPTION;
	}
}