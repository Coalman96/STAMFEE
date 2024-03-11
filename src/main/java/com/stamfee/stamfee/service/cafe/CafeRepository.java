package com.stamfee.stamfee.service.cafe;

import com.stamfee.stamfee.entity.Cafe;
import com.stamfee.stamfee.entity.CafeMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {

}
