package com.clone.apps.domains.members.services;

import com.clone.apps.commons.errors.BusinessException;
import com.clone.apps.commons.errors.ErrorCode;
import com.clone.apps.domains.members.persistences.entity.Member;
import com.clone.apps.domains.members.persistences.entity.MemberAuthentication;
import com.clone.apps.domains.members.persistences.repository.MemberAuthenticationRepository;
import com.clone.apps.domains.members.persistences.repository.MemberRepository;
import com.clone.apps.web.vo.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by kh.jin on 2019. 9. 11.
 */

@Service
public class SignupService implements ApplicationEventPublisherAware {

    private final Logger log = LoggerFactory.getLogger(SignupService.class);

    private MemberRepository memberRepository;

    private MemberAuthenticationRepository memberAuthenticationRepository;

    private ApplicationEventPublisher publisher;

    @Autowired
    public SignupService(final MemberRepository memberRepository,
                         final MemberAuthenticationRepository memberAuthenticationRepository) {
        this.memberRepository = memberRepository;
        this.memberAuthenticationRepository = memberAuthenticationRepository;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Transactional
    public Member signup(SignupRequest request) {
        // Email 중복
        Optional.ofNullable(memberRepository.findByEmail(request.getEmail()))
                .ifPresent(m -> {
                    throw new BusinessException(ErrorCode.DUPLICATE_DATA);
                });

        Member member = Member
                .builder()
                .email(request.getEmail())
                .name(request.getName())
                .birthday(request.getBirthday())
                .telecomCompanyCode(request.getTelecomCompanyCode())
                .mobileNumber(request.getMobileNumber())
                .profileImageUrl(request.getProfileImageUrl())
                .build();
        memberRepository.save(member);
        log.debug("member id : {}", member.getId());

        MemberAuthentication authentication = MemberAuthentication
                .builder()
                .id(member.getId())
                .pwd(request.getPassword())
                .build();
        log.debug("authentication : {}", authentication);
        memberAuthenticationRepository.save(authentication);

        publisher.publishEvent(new SignupEvent(member.getEmail()));

        log.info("result : {}", member);
        return member;
    }
}