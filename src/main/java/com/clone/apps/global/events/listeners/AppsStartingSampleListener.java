package com.clone.apps.global.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by kh.jin on 2019. 7. 17.
 */

@Component
public class AppsStartingSampleListener implements ApplicationListener<ApplicationStartingEvent> {

    private final Logger log = LoggerFactory.getLogger(AppsStartingSampleListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
        log.info("############### apps starting.");
    }
}