package net.joaopeixoto.geode.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;



@ImportResource("classpath:cache-config.xml")
@Configuration
@EnableGemfireFunctionExecutions
@EnableGemfireFunctions
public class ServerConfig {
}
