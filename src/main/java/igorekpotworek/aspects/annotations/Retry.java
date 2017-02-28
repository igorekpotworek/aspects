package igorekpotworek.aspects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Retry {

	public int attempts() default 3;

	public long timeout() default 1000;

	public Class<?> exceptionClass() default Throwable.class;

}