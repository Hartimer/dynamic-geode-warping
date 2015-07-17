package net.joaopeixoto.geode.shell;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

@Component
@Order(value=Ordered.HIGHEST_PRECEDENCE)
public class GeodeshPromptProvider implements PromptProvider {

    @Override
    public String getProviderName() {
        return "geodesh-prompt-provider";
    }

    @Override
    public String getPrompt() {
        return "geodesh> ";
    }

}
