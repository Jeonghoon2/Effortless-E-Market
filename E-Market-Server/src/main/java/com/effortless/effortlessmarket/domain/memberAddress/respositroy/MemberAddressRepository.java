package com.effortless.effortlessmarket.domain.memberAddress.respositroy;

import com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAddressRepository extends JpaRepository<MemberAddress,Long> {

    Optional<MemberAddress> findById(Long id);
    List<MemberAddress> findByMemberId(Long id);
}
