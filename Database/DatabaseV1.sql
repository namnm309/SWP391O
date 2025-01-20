-- Bảng Quản trị viên (Admin)
CREATE TABLE tbl_admin (
    admin_id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone INT CHECK (phone >= 1000000000 AND phone <= 9999999999),
    role VARCHAR(50) CHECK (role IN ('Super Admin', 'Admin')) NOT NULL
);

-- Bảng Nhân viên (Staff)
CREATE TABLE tbl_staff (
    staff_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone INT CHECK (phone >= 1000000000 AND phone <= 9999999999),
    position VARCHAR(50) CHECK (position IN ('Doctor', 'Nurse', 'Receptionist')) NOT NULL,
    salary NUMERIC(10, 2),
    status VARCHAR(50) CHECK (status IN ('Active', 'Inactive')) DEFAULT 'Active',
    admin_id INT,
    FOREIGN KEY (admin_id) REFERENCES tbl_admin(admin_id)
);

-- Bảng Khách hàng (Customer)
CREATE TABLE tbl_customer (
    customer_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone INT CHECK (phone >= 1000000000 AND phone <= 9999999999),
    address VARCHAR(255),
    is_verified BOOLEAN DEFAULT FALSE,
    registered_date DATE DEFAULT CURRENT_DATE
);

-- Bảng Mã OTP (OTP Codes)
CREATE TABLE tbl_otp (
    otp_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (customer_id) REFERENCES tbl_customer(customer_id)
);

-- Bảng Trẻ em (Child)
CREATE TABLE tbl_child (
    child_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(50) CHECK (gender IN ('Male', 'Female', 'Other')) NOT NULL,
    health_notes TEXT,
    FOREIGN KEY (customer_id) REFERENCES tbl_customer(customer_id)
);

-- Bảng Dịch vụ vaccine gói (Package Vaccination Service)
CREATE TABLE tbl_package_vaccination_service (
    service_id SERIAL PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    age_group VARCHAR(50)
);

-- Bảng Dịch vụ vaccine lẻ (Single Vaccination Service)
CREATE TABLE tbl_single_vaccination_service (
    service_id SERIAL PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    age_group VARCHAR(50)
);

-- Bảng Lịch hẹn tiêm chủng (Appointment)
CREATE TABLE tbl_appointment (
    appointment_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    child_id INT NOT NULL,
    package_service_id INT,
    single_service_id INT,
    appointment_date DATE NOT NULL,
    status VARCHAR(50) CHECK (status IN ('Pending', 'Completed', 'Cancelled')) DEFAULT 'Pending',
    staff_id INT,
    FOREIGN KEY (customer_id) REFERENCES tbl_customer(customer_id),
    FOREIGN KEY (child_id) REFERENCES tbl_child(child_id),
    FOREIGN KEY (package_service_id) REFERENCES tbl_package_vaccination_service(service_id),
    FOREIGN KEY (single_service_id) REFERENCES tbl_single_vaccination_service(service_id),
    FOREIGN KEY (staff_id) REFERENCES tbl_staff(staff_id)
);

-- Bảng Hồ sơ tiêm chủng (Vaccination Record)
CREATE TABLE tbl_vaccination_record (
    record_id SERIAL PRIMARY KEY,
    child_id INT NOT NULL,
    package_service_id INT,
    single_service_id INT,
    staff_id INT NOT NULL,
    vaccination_date DATE NOT NULL,
    vaccine_batch_number VARCHAR(50),
    notes TEXT,
    FOREIGN KEY (child_id) REFERENCES tbl_child(child_id),
    FOREIGN KEY (package_service_id) REFERENCES tbl_package_vaccination_service(service_id),
    FOREIGN KEY (single_service_id) REFERENCES tbl_single_vaccination_service(service_id),
    FOREIGN KEY (staff_id) REFERENCES tbl_staff(staff_id)
);

-- Bảng Phản ứng sau tiêm (Reaction)
CREATE TABLE tbl_reaction (
    reaction_id SERIAL PRIMARY KEY,
    record_id INT NOT NULL,
    reaction_details TEXT NOT NULL,
    severity VARCHAR(50) CHECK (severity IN ('Mild', 'Moderate', 'Severe')) NOT NULL,
    reported_date DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (record_id) REFERENCES tbl_vaccination_record(record_id)
);

-- Bảng Thanh toán (Payment)
CREATE TABLE tbl_payment (
    payment_id SERIAL PRIMARY KEY,
    appointment_id INT NOT NULL,
    payment_date DATE DEFAULT CURRENT_DATE,
    amount NUMERIC(10, 2) NOT NULL,
    status VARCHAR(50) CHECK (status IN ('Paid', 'Unpaid', 'Refunded')) DEFAULT 'Unpaid',
    FOREIGN KEY (appointment_id) REFERENCES tbl_appointment(appointment_id)
);

-- Bảng Đánh giá và phản hồi (Rating & Feedback)
CREATE TABLE tbl_rating_feedback (
    feedback_id SERIAL PRIMARY KEY,
    appointment_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    FOREIGN KEY (appointment_id) REFERENCES tbl_appointment(appointment_id)
);

-- Dữ liệu mẫu
INSERT INTO tbl_package_vaccination_service (service_name, description, price, age_group) VALUES
('Newborn Package', 'Comprehensive vaccination package for newborns', 200.00, '0-12 months'),
('Toddler Package', 'Comprehensive vaccination package for toddlers', 300.00, '1-3 years');

INSERT INTO tbl_single_vaccination_service (service_name, description, price, age_group) VALUES
('Hepatitis B - Single', 'Hepatitis B vaccine for newborns', 20.00, '0-1 month'),
('MMR - Single', 'Measles, Mumps, Rubella vaccine', 50.00, '12-15 months');

-- Các dữ liệu mẫu khác (Admin, Staff, Customer, Child, Appointment, Vaccination Record, Reaction, Payment, Rating)
INSERT INTO tbl_admin (username, password, full_name, email, phone, role) VALUES
('admin1', 'password123', 'Admin One', 'admin1@example.com', 1234567890, 'Super Admin');

INSERT INTO tbl_staff (full_name, email, password, phone, position, salary, status, admin_id) VALUES
('Dr. John Doe', 'johndoe@example.com', 'password123', 1234567891, 'Doctor', 1000.00, 'Active', 1),
('Nurse Jane Smith', 'janesmith@example.com', 'password123', 1234567892, 'Nurse', 800.00, 'Active', 1);

INSERT INTO tbl_customer (full_name, email, phone, address, is_verified) VALUES
('Alice Johnson', 'alicej@example.com', 1234567893, '123 Main St', TRUE),
('Bob Brown', 'bobbrown@example.com', 1234567894, '456 Elm St', TRUE);

INSERT INTO tbl_child (customer_id, full_name, date_of_birth, gender, health_notes) VALUES
(1, 'Charlie Johnson', '2020-05-15', 'Male', 'Healthy'),
(2, 'Daisy Brown', '2019-11-20', 'Female', 'Allergic to penicillin');

INSERT INTO tbl_appointment (customer_id, child_id, package_service_id, single_service_id, appointment_date, status, staff_id) VALUES
(1, 1, NULL, 1, '2025-01-15', 'Pending', 1),
(2, 2, 1, NULL, '2025-01-20', 'Pending', 2);

INSERT INTO tbl_vaccination_record (child_id, package_service_id, single_service_id, staff_id, vaccination_date, vaccine_batch_number, notes) VALUES
(1, NULL, 1, 1, '2025-01-15', 'HB12345', 'No issues'),
(2, 1, NULL, 2, '2025-01-20', 'PKG67890', 'Mild fever');

INSERT INTO tbl_reaction (record_id, reaction_details, severity, reported_date) VALUES
(1, 'Slight swelling at injection site', 'Mild', '2025-01-16'),
(2, 'Fever lasting 2 days', 'Moderate', '2025-01-21');

INSERT INTO tbl_payment (appointment_id, payment_date, amount, status) VALUES
(1, '2025-01-15', 20.00, 'Paid'),
(2, '2025-01-20', 300.00, 'Paid');

INSERT INTO tbl_rating_feedback (appointment_id, rating, comments) VALUES
(1, 5, 'Excellent service!'),
(2, 4, 'Good but room for improvement.');