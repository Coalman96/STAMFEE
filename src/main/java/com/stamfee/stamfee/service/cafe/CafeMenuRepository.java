package com.stamfee.stamfee.service.cafe;

import com.stamfee.stamfee.entity.CafeMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeMenuRepository extends JpaRepository<CafeMenu, Long> {

    List<CafeMenu> findByCafeId(Long cafeId);

}
