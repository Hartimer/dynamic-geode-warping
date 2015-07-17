package net.joaopeixoto.geode.shell;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.BannerProvider;
import org.springframework.stereotype.Component;

@Component
@Order(value=Ordered.HIGHEST_PRECEDENCE)
public class GeodeshBannerProvider implements BannerProvider {

    @Override
    public String getProviderName() {
        return "geodesh-banner-provider";
    }

    @Override
    public String getBanner() {
        return "\n=============================\n" +
               "   GEODESH - Geode Shell   \n" +
                 "=============================";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome to everybody!\n";
    }

}
