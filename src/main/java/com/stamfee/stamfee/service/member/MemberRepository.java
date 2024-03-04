package com.stamfee.stamfee.service.member;

import com.stamfee.stamfee.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

  Optional<Member> findByCellphone(String cellphone);


}
