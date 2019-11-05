package com.clone.apps.domain.member.service;

import com.clone.apps.domain.member.persistence.entity.Member;
import com.clone.apps.domain.member.persistence.entity.MemberAuthentication;
import com.clone.apps.domain.member.persistence.repository.MemberAuthenticationRepository;
import com.clone.apps.domain.member.persistence.repository.MemberRepository;
import com.clone.apps.global.errors.LoginException;
import com.clone.apps.web.vo.LoginRequest;
import com.clone.apps.web.vo.LoginResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Created by kh.jin on 2019. 9. 8.
 */
@Service
public class LoginService {
    private final Logger log = LoggerFactory.getLogger(LoginService.class);

    private MemberRepository memberRepository;
    private MemberAuthenticationRepository memberAuthenticationRepository;
    private MemberBlockedService memberBlockedService;

    @Autowired
    public LoginService(@Qualifier("memberRepository") final MemberRepository memberRepository,
                        @Qualifier("memberAuthenticationRepository") final MemberAuthenticationRepository memberAuthenticationRepository,
                        @Qualifier("memberBlockedService") final MemberBlockedService memberBlockedService) {
        this.memberRepository = memberRepository;
        this.memberAuthenticationRepository = memberAuthenticationRepository;
        this.memberBlockedService = memberBlockedService;
    }

    public LoginResult login(LoginRequest request) throws NoSuchAlgorithmException {
        Member member = memberRepository.findByEmail(request.getEmail());
        Optional.ofNullable(member).orElseThrow(LoginException::new);
        log.debug("member : {}", member);

        Optional.ofNullable(member).map(Member::getMemberAuthentication).orElseThrow(LoginException::new);
        MemberAuthentication authentication = member.getMemberAuthentication();
        log.debug("member authentication : {}", authentication);

        if (!authentication.authenticate(request.getPassword())) {
            memberBlockedService.blocked(authentication);
            // TODO : History Table 생성 저장

            return LoginResult
                    .builder()
                    .result(false)
                    .failedCnt(authentication.getLoginFailedCount())
                    .memberStatus(authentication.getStatusCode())
                    .build();
        }
        authentication.initLoginFailedCount();
        memberAuthenticationRepository.save(authentication);

        // Login History
        return LoginResult
                .builder()
                .result(true)
                .member(member)
                .build();
    }
}