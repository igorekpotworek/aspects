package igorekpotworek.aspects;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import igorekpotworek.aspects.aspects.HandleExceptionsAspect;
import igorekpotworek.aspects.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, TestService.class })
public class RetryAspectTest {

	static final IOException EXCEPTION = new IOException("Some test exception");
	static final long TIMEOUT = 100l;
	static final int ATTEMPTS = 3;

	private ArgumentCaptor<String> warnCaptor;
	@Mock
	private Logger logger;

	@Autowired
	private TestService testService;

	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		setFinalStatic(HandleExceptionsAspect.class.getDeclaredField("log"), logger);
		warnCaptor = ArgumentCaptor.forClass(String.class);
	}

	@Test
	public void testMethodWithExceptionThatShouldRetry() {

		long start = System.currentTimeMillis();

		try {
			testService.testMethodWithExceptionThatShouldRetry();
		} catch (Exception e) {
			assertThat(e, instanceOf(EXCEPTION.getClass()));
		}

		long end = System.currentTimeMillis();
		long duration = end - start;

		assertThat(duration, greaterThan(ATTEMPTS * TIMEOUT));
		verify(logger, times(ATTEMPTS)).warning(warnCaptor.capture());
	}

	@Test
	public void testMethodWithExceptionThatShouldntRetry() {

		try {
			testService.testMethodWithExceptionThatShouldntRetry();
		} catch (Exception e) {
			assertThat(e, instanceOf(EXCEPTION.getClass()));
		}

		verify(logger, never()).warning(warnCaptor.capture());
	}

	private static void setFinalStatic(Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}

}
