-- Initial PostgreSQL schema for the school management application.
-- IDs remain strings at the API boundary and are generated as UUID text by JPA.

CREATE TABLE users (
    id varchar(36) PRIMARY KEY,
    adhar_number varchar(12) UNIQUE,
    user_id varchar(255) UNIQUE,
    full_name varchar(255),
    email varchar(320) NOT NULL UNIQUE,
    password varchar(255),
    mobile varchar(32) NOT NULL UNIQUE,
    profile_image_url text,
    status varchar(32),
    role_name varchar(64),
    permissions jsonb,
    class_id varchar(64),
    department_id varchar(128),
    tenant_id varchar(128),
    created_at timestamptz,
    updated_at timestamptz,
    password_changed_at timestamptz
);

CREATE TABLE roles (
    id varchar(36) PRIMARY KEY,
    name varchar(64),
    description text,
    permissions jsonb,
    is_default boolean NOT NULL DEFAULT false,
    tenant_id varchar(128),
    created_at timestamptz,
    updated_at timestamptz,
    created_by varchar(36),
    updated_by varchar(36),
    CONSTRAINT uk_role_name_tenant UNIQUE (name, tenant_id)
);

CREATE TABLE password_reset_tokens (
    id varchar(36) PRIMARY KEY,
    user_id varchar(36),
    token varchar(512),
    expiry timestamptz,
    used boolean NOT NULL DEFAULT false,
    tenant_id varchar(128)
);

CREATE TABLE otp_logs (
    id varchar(36) PRIMARY KEY,
    user_id varchar(36),
    otp varchar(32),
    expiry timestamptz,
    used boolean NOT NULL DEFAULT false,
    purpose varchar(64),
    tenant_id varchar(128)
);

CREATE TABLE schools (
    id varchar(36) PRIMARY KEY,
    slug varchar(128) UNIQUE,
    school_name varchar(255),
    short_name varchar(128),
    tagline text,
    description text,
    branding jsonb,
    contact jsonb,
    address jsonb,
    management jsonb,
    registration jsonb,
    status varchar(64),
    created_at timestamp,
    updated_at timestamp,
    created_by varchar(36),
    updated_by varchar(36)
);

CREATE TABLE classes (
    id varchar(128) PRIMARY KEY,
    display_name varchar(255),
    program varchar(128),
    display_order integer NOT NULL DEFAULT 0,
    sections jsonb,
    subject_names jsonb
);

CREATE TABLE academic_years (
    id varchar(36) PRIMARY KEY,
    name varchar(32) NOT NULL UNIQUE,
    start_date date,
    end_date date,
    is_active boolean NOT NULL DEFAULT false
);

CREATE TABLE subjects (
    id varchar(36) PRIMARY KEY,
    name varchar(255),
    code varchar(64),
    class_id varchar(128),
    is_optional boolean NOT NULL DEFAULT false
);

CREATE TABLE students (
    id varchar(36) PRIMARY KEY,
    first_name varchar(128),
    middle_name varchar(128),
    last_name varchar(128),
    date_of_birth date,
    gender varchar(32),
    adhar_number varchar(12) UNIQUE,
    photo_url text,
    admission_number varchar(64) UNIQUE,
    admission_year integer,
    status varchar(32),
    current_class_id varchar(128),
    current_section varchar(64),
    phone varchar(32),
    email varchar(320),
    permanent_address jsonb,
    present_address jsonb,
    health_record jsonb,
    created_by varchar(36),
    updated_by varchar(36),
    created_at bigint NOT NULL DEFAULT 0,
    updated_at bigint NOT NULL DEFAULT 0,
    current_academic_year varchar(32),
    attributes jsonb
);

CREATE TABLE student_guardians (
    student_id varchar(36) NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    guardian_order integer NOT NULL,
    adhar_number varchar(12),
    name varchar(255),
    relation varchar(64),
    phone varchar(32),
    email varchar(320),
    iam_user_id varchar(36),
    is_primary boolean NOT NULL DEFAULT false,
    PRIMARY KEY (student_id, guardian_order)
);

CREATE TABLE student_academic_history (
    id varchar(36) PRIMARY KEY,
    student_id varchar(36) NOT NULL REFERENCES students(id),
    academic_year varchar(32),
    class_id varchar(128),
    section varchar(64),
    result varchar(64),
    promoted_at bigint NOT NULL DEFAULT 0,
    promoted_by varchar(36)
);

CREATE TABLE student_documents (
    id varchar(36) PRIMARY KEY,
    student_id varchar(36) NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    filename varchar(255),
    storage_key varchar(512),
    doc_type varchar(64),
    uploaded_at bigint NOT NULL DEFAULT 0,
    uploaded_by varchar(36)
);

CREATE TABLE staff (
    id varchar(36) PRIMARY KEY,
    full_name varchar(255),
    email varchar(320),
    mobile varchar(32),
    gender varchar(32),
    date_of_birth date,
    adhaar varchar(12) UNIQUE,
    staff_type varchar(64),
    designation varchar(128),
    joining_date date,
    employee_code varchar(64) UNIQUE,
    subjects jsonb,
    assigned_class_ids jsonb,
    tenant_id varchar(128),
    status varchar(32)
);

CREATE TABLE attendance (
    id varchar(36) PRIMARY KEY,
    user_id varchar(36),
    user_type varchar(32),
    class_id varchar(128),
    section varchar(64),
    current_academic_year varchar(32),
    department varchar(128),
    designation varchar(128),
    date date,
    status varchar(32),
    remarks text,
    marked_by varchar(36),
    name_snapshot varchar(255),
    created_at timestamptz,
    updated_at timestamptz,
    staff_type varchar(64),
    CONSTRAINT uk_attendance_user_date UNIQUE (user_id, date)
);

CREATE TABLE leave_requests (
    id varchar(36) PRIMARY KEY,
    user_id varchar(36),
    user_name varchar(255),
    user_type varchar(32),
    role varchar(64),
    start_date date,
    end_date date,
    days integer NOT NULL,
    leave_type varchar(64),
    reason text,
    status varchar(32),
    applied_on timestamp,
    rejection_reason text
);

CREATE TABLE fees (
    id varchar(36) PRIMARY KEY,
    student_id varchar(36) NOT NULL REFERENCES students(id),
    academic_year varchar(32) NOT NULL,
    total_amount numeric(12,2),
    paid_amount numeric(12,2),
    due_amount numeric(12,2),
    status varchar(32),
    CONSTRAINT uk_fee_student_year UNIQUE (student_id, academic_year)
);

CREATE TABLE fee_items (
    fee_id varchar(36) NOT NULL REFERENCES fees(id) ON DELETE CASCADE,
    item_order integer NOT NULL,
    name varchar(255),
    amount numeric(12,2),
    PRIMARY KEY (fee_id, item_order)
);

CREATE TABLE fee_payments (
    fee_id varchar(36) NOT NULL REFERENCES fees(id) ON DELETE CASCADE,
    payment_order integer NOT NULL,
    receipt_no varchar(128) UNIQUE,
    amount_paid numeric(12,2),
    mode varchar(64),
    collected_by varchar(36),
    paid_at timestamp,
    PRIMARY KEY (fee_id, payment_order)
);

CREATE TABLE class_fee_masters (
    id varchar(36) PRIMARY KEY,
    class_id varchar(128),
    academic_year varchar(32),
    fee_items jsonb,
    total_amount numeric(12,2)
);

CREATE TABLE exam_definitions (
    id varchar(36) PRIMARY KEY,
    name varchar(255),
    academic_year varchar(32),
    term varchar(64),
    is_active boolean NOT NULL DEFAULT false,
    is_published boolean NOT NULL DEFAULT false,
    start_date timestamptz,
    end_date timestamptz,
    class_configs jsonb,
    created_at timestamp,
    updated_at timestamp
);

CREATE TABLE exam_results (
    id varchar(36) PRIMARY KEY,
    student_id varchar(36) REFERENCES students(id),
    academic_year varchar(32),
    class_id varchar(128),
    exam_name varchar(255),
    subject_name varchar(255),
    marks_obtained double precision NOT NULL,
    total_marks double precision NOT NULL,
    grade varchar(32),
    remarks text
);

CREATE TABLE student_achievements (
    id varchar(36) PRIMARY KEY,
    student_id varchar(36) REFERENCES students(id),
    academic_year varchar(32),
    title varchar(255),
    category varchar(64),
    description text,
    date date,
    certificate_url text
);

CREATE TABLE salary_structures (
    id varchar(36) PRIMARY KEY,
    staff_id varchar(36) REFERENCES staff(id),
    staff_name varchar(255),
    basic_salary double precision,
    hra double precision,
    da double precision,
    transport_allowance double precision,
    special_allowance double precision,
    provident_fund double precision,
    professional_tax double precision,
    gross_salary double precision,
    net_salary double precision
);

CREATE TABLE payroll_transactions (
    id varchar(36) PRIMARY KEY,
    staff_id varchar(36) REFERENCES staff(id),
    staff_name varchar(255),
    month varchar(32),
    year integer,
    total_days_in_month integer,
    present_days integer,
    paid_leaves integer,
    absent_days integer,
    payable_days integer,
    basic_earned double precision,
    hra_earned double precision,
    total_earnings double precision,
    total_deductions double precision,
    net_payable double precision,
    status varchar(32),
    payment_date date,
    generated_date date
);

CREATE TABLE audit_logs (
    id varchar(36) PRIMARY KEY,
    tenant_id varchar(128),
    entity_name varchar(128),
    entity_id varchar(36),
    action varchar(64),
    performed_by_user_id varchar(36),
    performed_by_role varchar(64),
    performed_at timestamptz,
    old_value jsonb,
    new_value jsonb,
    message text,
    expiry_at timestamptz
);

CREATE INDEX idx_students_class_section ON students (current_class_id, current_section);
CREATE INDEX idx_students_academic_year ON students (current_academic_year);
CREATE INDEX idx_attendance_date_type ON attendance (date, user_type);
CREATE INDEX idx_attendance_class_date ON attendance (class_id, section, date);
CREATE INDEX idx_fee_year_status ON fees (academic_year, status);
CREATE INDEX idx_exam_results_student_year ON exam_results (student_id, academic_year);
CREATE INDEX idx_audit_expiry ON audit_logs (expiry_at);
CREATE UNIQUE INDEX uk_academic_year_single_active ON academic_years (is_active) WHERE is_active = true;
