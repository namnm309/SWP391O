--dinh nghia thuoc tinh gender va thuoc tinh role cho bang tbl_users
CREATE type gender_user as ENUM ('male','female');
CREATE type role_user as ENUM ('admin','staff','customer','child');

--tbl_user
CREATE TABLE tbl_users (
	user_id SERIAL PRIMARY KEY,
	parent_id INT REFERENCES tbl_users(user_id),
	username varchar(50) unique, --ten login , duy nhat doc ton 
	fullname varchar(100),
	password varchar(255), -- password login 
	email varchar(50) unique ,
	phone varchar(50) unique not null, --phone dang nhap otp , duy nhat doc ton 
	birth_date date not null, --ngay sinh bat buot 
	gender gender_user not null,--doi voi postgres phai dinh nghia truoc chu ko cho dong thoi 
	role role_user not null
	
);

--tbi_vaccines
CREATE TABLE tbl_vaccines (
	vaccine_id SERIAL PRIMARY KEY,
	vaccine_name varchar(100) not null,
	description TEXT,
	vaccine_age varchar(50), --do tuoi thich hop cho vaccine 
	price DECIMAL(10,2) not null,
	date_of_manufacture date not null,
	vaccine_expiry_date varchar(20) --vi du 24 thang 

);

--tbl_services
--dinh nghia loai service service_type
CREATE TYPE service_type AS ENUM ('single','combo','modify');--le , combo va tuy chon 

CREATE TABLE tbl_services (
	service_id SERIAL PRIMARY KEY,
	service_name varchar(100) not null,
	description TEXT,
	type service_type not null,
	price DECIMAL(10,2) not null,
	date_create date
);

--tbl_appointments
--dinh nghia tinh trang lich hen 
CREATE TYPE appointment_status AS ENUM ('in process','completed','cancelles'); 
--dinh nghia tinh trang thanh toan 
CREATE TYPE appointment_payment_status AS ENUM ('unpaid','paid');

CREATE TABLE tbl_appointments (
	appointment_id SERIAL PRIMARY KEY,
	user_id INT REFERENCES tbl_users(user_id) ON DELETE CASCADE,
	--On Delete Cascade giup tu dong cac record lq den user_id khi user_id bi xoa
	vaccine_id INT REFERENCES tbl_vaccines(vaccine_id) ON DELETE CASCADE,
	appointment_date TIMESTAMP not null,--timestamp = vd 2025/1/1 00:00:00
	status appointment_status DEFAULT 'in process',
	payment_status appointment_payment_status DEFAULT 'unpaid'
	
);

--tbl_vaccine_records
CREATE TABLE tbl_vaccine_records(
	record_id SERIAL PRIMARY KEY,
	user_id INT REFERENCES tbl_users(user_id) ON DELETE CASCADE,
	appointment_id INT REFERENCES tbl_appointments(appointment_id) ON DELETE CASCADE,
	date_record date not null,--ngay tiem 
	reaction_record text 
);

--tbl_payments
--dinh nghia cho phuong thuc thanh toan
CREATE TYPE payment_method AS ENUM ('cash', 'credit_card', 'momo'); 

CREATE TABLE tbl_payments (
    payment_id SERIAL PRIMARY KEY,     
    user_id INT REFERENCES tbl_users(user_id) ON DELETE CASCADE, 
    appointment_id INT REFERENCES tbl_appointments(appointment_id) ON DELETE CASCADE, 
    amount DECIMAL(10, 2) not null,    
    payment_date TIMESTAMP DEFAULT NOW(), 
    method_payment payment_method not null 
);

--tbl_feedback
CREATE TABLE tbl_feedback (
    feedback_id SERIAL PRIMARY KEY,    
    user_id INT REFERENCES tbl_users(user_id) ON DELETE CASCADE, 
    appointment_id INT REFERENCES tbl_appointments(appointment_id) ON DELETE CASCADE,
    rating INT CHECK (rating BETWEEN 1 AND 5), 
    comment TEXT,                     
    time_created TIMESTAMP DEFAULT NOW() 
);

--tbl_notifications
--dinh nghia trang thai thong bao 
CREATE TYPE notification_status_enum AS ENUM ('read', 'unread'); 

CREATE TABLE tbl_notifications (
    notification_id SERIAL PRIMARY KEY, 
    user_id INT REFERENCES tbl_users(user_id) ON DELETE CASCADE, 
    content TEXT NOT NULL,              
    sent_at TIMESTAMP DEFAULT NOW(),    
    status notification_status_enum DEFAULT 'unread' --trang thai thong bao nguoi dung da doc chua 
);

--tbl_otp
CREATE TABLE tbl_otp (
    otp_id SERIAL PRIMARY KEY, 
    phone VARCHAR(15) NOT NULL,--sdt gui otp
    otp_code VARCHAR(6) NOT NULL,--khi co y/c tu fe => khoi tao => cung cap ma nay cho sdt => user nhap vao => compare => lum 
    created_at TIMESTAMP DEFAULT NOW(),--thoi gian tao otp => gioi han otp trong vong bao nhieu phut 
    expires_at TIMESTAMP NOT NULL,
    otp_status BOOLEAN DEFAULT FALSE  --trang thai otp da su dung hay chua , neu roi huy bo ma 
);


--Bang trung gian giua tbl_service va tbl_vaccine
CREATE TABLE tbl_service_vaccines (
    id SERIAL PRIMARY KEY,                  
    service_id INT REFERENCES tbl_services(service_id) ON DELETE CASCADE, 
	  vaccine_id INT REFERENCES tbl_vaccines(vaccine_id) ON DELETE CASCADE
);



