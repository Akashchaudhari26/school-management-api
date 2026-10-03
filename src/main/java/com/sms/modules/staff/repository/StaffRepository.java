package com.sms.modules.staff.repository;

import com.sms.modules.staff.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff> {

    List<Staff> findByStaffType(String staffType);

    List<Staff> findByTenantId(String tenantId);

    Staff findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(String prefix);
}
