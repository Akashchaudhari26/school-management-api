package com.sms.modules.staff.repository;

import com.sms.modules.staff.domain.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends MongoRepository<Staff, String> {

    List<Staff> findByStaffType(String staffType);

    List<Staff> findByTenantId(String tenantId);
}
