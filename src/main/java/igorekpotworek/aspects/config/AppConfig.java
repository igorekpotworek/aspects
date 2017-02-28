package igorekpotworek.aspects.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = { "igorekpotworek.aspects" })
@EnableAspectJAutoProxy
public class AppConfig {

}
