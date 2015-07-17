package net.joaopeixoto.geode.server;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;



@Component
public class LoggingCacheListener extends CacheListenerAdapter {
    private static Log log = LogFactory.getLog(LoggingCacheListener.class);


    @Override
    public void afterCreate(EntryEvent event) {
        final String regionName = event.getRegion().getName();
        final Object key = event.getKey();
        final Object newValue = event.getNewValue();
        log.info("In region [" + regionName + "] created key [" + key
                         + "] value [" + newValue + "]");
    }

    @Override
    public void afterDestroy(EntryEvent event) {
        final String regionName = event.getRegion().getName();
        final Object key = event.getKey();
        log.info("In region [" + regionName + "] destroyed key [" + key
                         + "]");
    }

    @Override
    public void afterUpdate(EntryEvent event) {
        final String regionName = event.getRegion().getName();
        final Object key = event.getKey();
        final Object newValue = event.getNewValue();
        final Object oldValue = event.getOldValue();
        log.info("In region [" + regionName + "] updated key [" + key
                         + "] [oldValue [" + oldValue + "] new value [" + newValue +"]");
    }
}
