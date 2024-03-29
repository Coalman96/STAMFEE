package com.stamfee.stamfee.service.stamp;

import com.stamfee.stamfee.entity.Stamp;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {

}
