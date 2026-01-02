package com.sms.modules.attendance.repository;

import com.sms.modules.attendance.domain.Attendance;
import com.sms.modules.attendance.domain.UserType;
import com.sms.modules.student.domain.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    // Prevent duplicates
    Optional<Attendance> findByUserIdAndDate(String userId, LocalDate date);

    // For Bulk Marking checks
    List<Attendance> findByDateAndUserIdIn(LocalDate date, Collection<String> userIds);

    // CORRECT PAGINATION QUERIES
    Page<Attendance> findByUserId(String userId, Pageable pageable);

    List<Attendance> findByUserId(String userId);

    // This allows "Show me attendance for Class 10 - Section A on Date X"
    Page<Attendance> findByClassIdAndSectionAndDate(String classId, String section, LocalDate date, Pageable pageable);

    // Basic date filter
    Page<Attendance> findByDate(LocalDate date, Pageable pageable);

    List<Attendance> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    // 2. For Class View (Exact Match)
    List<Attendance> findByClassIdAndSectionAndDate(String classId, String section, LocalDate date);

    // 3. For Staff View (With and Without Department filter)
    Page<Attendance> findByDepartmentAndDate(String department, LocalDate date, Pageable pageable);

    Page<Attendance> findByUserTypeAndDate(UserType userType, LocalDate date, Pageable pageable);
    
    @Query("{ 'classId': ?0, 'section': ?1, 'date': { $gte: ?2, $lte: ?3 } }")
    List<Attendance> findByClassIdAndSectionIdAndDateBetween(String classId, String sectionId, LocalDate startDate, LocalDate endDate);
    
    List<Attendance> findByDateAfter(LocalDate date);
    
    @Query("{ 'userType': ?0, 'date': { $gte: ?1, $lte: ?2 } }")
    List<Attendance> findByUserTypeAndDateBetween(UserType userType, LocalDate startDate, LocalDate endDate);
  
}