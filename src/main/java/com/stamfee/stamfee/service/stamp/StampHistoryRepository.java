package com.stamfee.stamfee.service.stamp;

import com.stamfee.stamfee.entity.StampHistory;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.stereotype.Repository;

@Repository
public interface StampHistoryRepository extends JpaAttributeConverter<Long, StampHistory> {
}
