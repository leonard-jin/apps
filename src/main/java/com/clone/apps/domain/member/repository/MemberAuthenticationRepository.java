package com.clone.apps.domain.member.repository;

import com.clone.apps.entity.member.MemberAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kh.jin on 2019. 7. 19.
 */

@Repository
public interface MemberAuthenticationRepository extends JpaRepository<MemberAuthentication, Long> {
}