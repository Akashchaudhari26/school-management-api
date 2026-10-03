package com.sms.modules.attendance.repository;

import com.sms.modules.attendance.domain.Attendance;
import com.sms.modules.attendance.domain.UserType;
import com.sms.modules.student.domain.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {

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
    
    @Query("SELECT a FROM Attendance a WHERE a.classId = ?1 AND a.section = ?2 AND a.date BETWEEN ?3 AND ?4")
    List<Attendance> findByClassIdAndSectionIdAndDateBetween(String classId, String sectionId, LocalDate startDate, LocalDate endDate);
    
    List<Attendance> findByDateAfter(LocalDate date);
    
    @Query("SELECT a FROM Attendance a WHERE a.userType = ?1 AND a.date BETWEEN ?2 AND ?3")
    List<Attendance> findByUserTypeAndDateBetween(UserType userType, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.date = ?1 AND a.userType = ?2 GROUP BY a.status")
    List<Object[]> countByDateAndUserTypeGroupedByStatus(LocalDate date, UserType userType);
  
}
