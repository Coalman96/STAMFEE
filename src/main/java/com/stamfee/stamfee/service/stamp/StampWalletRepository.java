package com.stamfee.stamfee.service.stamp;

import com.stamfee.stamfee.entity.Cafe;
import com.stamfee.stamfee.entity.Member;
import com.stamfee.stamfee.entity.StampWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StampWalletRepository extends JpaRepository<StampWallet, Long> {

    StampWallet findByMemberAndCafe(Member member, Cafe cafe);

    Boolean existsByMemberAndCafe(Member member, Cafe cafe);

    List<StampWallet> findByMember(Member member);

}
