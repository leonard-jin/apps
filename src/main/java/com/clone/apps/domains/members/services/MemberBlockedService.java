package com.clone.apps.domains.members.services;

import com.clone.apps.domains.members.persistences.entity.MemberAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kh.jin on 2019. 9. 13.
 *
 */

@Service
public class MemberBlockedService extends AbstractMemberStatusService {
    private final Logger log = LoggerFactory.getLogger(MemberBlockedService.class);

    private final static int BLOCKED_CNT = 5;

    public MemberBlockedService() {
        super();
    }

    @Transactional
    public void blocked(MemberAuthentication authentication) {
        authentication.incrementLoginFailedCount();
        log.debug("failed cnt : {}", authentication.getLoginFailedCount());

        if (BLOCKED_CNT <= authentication.getLoginFailedCount()) {
            log.debug("User Blocked.");
            authentication.blocked();
        }
        updateStatus(authentication);
    }
}