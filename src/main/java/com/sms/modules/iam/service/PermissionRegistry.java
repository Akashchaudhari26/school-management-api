package com.sms.modules.iam.service;

import java.util.List;

import com.sms.modules.iam.domain.Permissions;

public final class PermissionRegistry {

    private PermissionRegistry() {
    }

    public static List<String> admin() {

        return List.of(
                Permissions.USER_VIEW,
                Permissions.USER_CREATE,
                Permissions.USER_UPDATE,

                Permissions.STUDENT_VIEW,
                Permissions.STUDENT_CREATE,
                Permissions.STUDENT_UPDATE,

                Permissions.TEACHER_VIEW,
                Permissions.TEACHER_CREATE,
                Permissions.TEACHER_UPDATE,

                Permissions.CLASS_VIEW,
                Permissions.CLASS_CREATE,
                Permissions.CLASS_UPDATE,

                Permissions.ATTENDANCE_VIEW,
                Permissions.ATTENDANCE_MARK,

                Permissions.EXAM_VIEW,
                Permissions.EXAM_CREATE,

                Permissions.FEE_VIEW,
                Permissions.FEE_CREATE,

                Permissions.REPORT_VIEW);
    }

    public static List<String> teacher() {

        return List.of(
                Permissions.STUDENT_VIEW,
                Permissions.ATTENDANCE_VIEW,
                Permissions.ATTENDANCE_MARK,
                Permissions.MARKS_VIEW,
                Permissions.MARKS_CREATE,
                Permissions.EXAM_VIEW);
    }

    public static List<String> principal() {

        return List.of(
                Permissions.STUDENT_VIEW,
                Permissions.STUDENT_CREATE,
                Permissions.STUDENT_UPDATE,
                Permissions.STUDENT_DELETE,
                Permissions.TEACHER_VIEW,
                Permissions.TEACHER_CREATE,
                Permissions.TEACHER_UPDATE,
                Permissions.CLASS_VIEW,
                Permissions.CLASS_CREATE,
                Permissions.CLASS_UPDATE,
                Permissions.ATTENDANCE_VIEW,
                Permissions.ATTENDANCE_MARK,
                Permissions.EXAM_VIEW,
                Permissions.EXAM_CREATE,
                Permissions.EXAM_UPDATE,
                Permissions.FEE_VIEW,
                Permissions.REPORT_VIEW,
                Permissions.SCHOOL_VIEW,
                Permissions.SCHOOL_UPDATE);
    }

    public static List<String> student() {

        return List.of(
                Permissions.STUDENT_VIEW,
                Permissions.ATTENDANCE_VIEW,
                Permissions.MARKS_VIEW,
                Permissions.FEE_VIEW,
                Permissions.EXAM_VIEW,
                Permissions.REPORT_VIEW);
    }

    public static List<String> parent() {

        return List.of(
                Permissions.STUDENT_VIEW,
                Permissions.ATTENDANCE_VIEW,
                Permissions.MARKS_VIEW,
                Permissions.FEE_VIEW,
                Permissions.REPORT_VIEW,
                Permissions.NOTIFICATION_VIEW);
    }

    public static List<String> accountant() {

        return List.of(
                Permissions.FEE_VIEW,
                Permissions.FEE_CREATE,
                Permissions.FEE_UPDATE,
                Permissions.PAYROLL_VIEW,
                Permissions.PAYROLL_CREATE,
                Permissions.PAYROLL_UPDATE,
                Permissions.REPORT_VIEW,
                Permissions.SCHOOL_VIEW);
    }

    public static List<String> receptionist() {

        return List.of(
                Permissions.STUDENT_VIEW,
                Permissions.STUDENT_CREATE,
                Permissions.PARENT_VIEW,
                Permissions.PARENT_CREATE,
                Permissions.CLASS_VIEW,
                Permissions.ATTENDANCE_VIEW,
                Permissions.FEE_VIEW,
                Permissions.NOTIFICATION_VIEW,
                Permissions.REPORT_VIEW);
    }

    public static List<String> all() {

        return List.of(
                Permissions.USER_VIEW,
                Permissions.USER_CREATE,
                Permissions.USER_UPDATE,
                Permissions.USER_DELETE,

                Permissions.ROLE_VIEW,
                Permissions.ROLE_CREATE,
                Permissions.ROLE_UPDATE,
                Permissions.ROLE_DELETE,

                Permissions.STUDENT_VIEW,
                Permissions.STUDENT_CREATE,
                Permissions.STUDENT_UPDATE,
                Permissions.STUDENT_DELETE,

                Permissions.TEACHER_VIEW,
                Permissions.TEACHER_CREATE,
                Permissions.TEACHER_UPDATE,
                Permissions.TEACHER_DELETE,

                Permissions.PARENT_VIEW,
                Permissions.PARENT_CREATE,
                Permissions.PARENT_UPDATE,
                Permissions.PARENT_DELETE,

                Permissions.CLASS_VIEW,
                Permissions.CLASS_CREATE,
                Permissions.CLASS_UPDATE,
                Permissions.CLASS_DELETE,

                Permissions.SUBJECT_VIEW,
                Permissions.SUBJECT_CREATE,
                Permissions.SUBJECT_UPDATE,
                Permissions.SUBJECT_DELETE,

                Permissions.ATTENDANCE_VIEW,
                Permissions.ATTENDANCE_MARK,
                Permissions.ATTENDANCE_UPDATE,
                Permissions.ATTENDANCE_DELETE,

                Permissions.EXAM_VIEW,
                Permissions.EXAM_CREATE,
                Permissions.EXAM_UPDATE,
                Permissions.EXAM_DELETE,

                Permissions.MARKS_VIEW,
                Permissions.MARKS_CREATE,
                Permissions.MARKS_UPDATE,
                Permissions.MARKS_DELETE,

                Permissions.FEE_VIEW,
                Permissions.FEE_CREATE,
                Permissions.FEE_UPDATE,
                Permissions.FEE_DELETE,

                Permissions.TIMETABLE_VIEW,
                Permissions.TIMETABLE_CREATE,
                Permissions.TIMETABLE_UPDATE,
                Permissions.TIMETABLE_DELETE,

                Permissions.LIBRARY_VIEW,
                Permissions.LIBRARY_CREATE,
                Permissions.LIBRARY_UPDATE,
                Permissions.LIBRARY_DELETE,

                Permissions.TRANSPORT_VIEW,
                Permissions.TRANSPORT_CREATE,
                Permissions.TRANSPORT_UPDATE,
                Permissions.TRANSPORT_DELETE,

                Permissions.PAYROLL_VIEW,
                Permissions.PAYROLL_CREATE,
                Permissions.PAYROLL_UPDATE,
                Permissions.PAYROLL_DELETE,

                Permissions.REPORT_VIEW,
                Permissions.REPORT_EXPORT,

                Permissions.NOTIFICATION_VIEW,
                Permissions.NOTIFICATION_SEND,

                Permissions.SCHOOL_VIEW,
                Permissions.SCHOOL_UPDATE,

                Permissions.SYSTEM_SETTINGS_VIEW,
                Permissions.SYSTEM_SETTINGS_UPDATE);
    }
}