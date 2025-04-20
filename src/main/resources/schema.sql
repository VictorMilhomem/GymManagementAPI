DROP TABLE IF EXISTS tb_students;

-- Create the table for students
CREATE TABLE tb_students (
                             id UUID PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             age INTEGER NOT NULL,
                             gender VARCHAR(50),
                             address TEXT,
                             phone VARCHAR(50),
                             email VARCHAR(255),
                             belt VARCHAR(50),
                             created_at DATE NOT NULL
);

-- Insert mock data for testing
INSERT INTO tb_students (id, name, age, gender, address, phone, email, belt, created_at) VALUES
(gen_random_uuid(), 'John Smith', 28, 'Male', '123 Main St, New York, NY', '212-555-1234', 'john.smith@email.com', 'Black', CURRENT_DATE),
(gen_random_uuid(), 'Emma Johnson', 24, 'Female', '456 Oak Ave, Los Angeles, CA', '310-555-2345', 'emma.j@email.com', 'Brown', CURRENT_DATE),
(gen_random_uuid(), 'Michael Davis', 35, 'Male', '789 Pine Blvd, Chicago, IL', '312-555-3456', 'michael.d@email.com', 'Purple', CURRENT_DATE),
(gen_random_uuid(), 'Sophia Miller', 19, 'Female', '101 Cedar Ln, Houston, TX', '713-555-4567', 'sophia.m@email.com', 'White', CURRENT_DATE),
(gen_random_uuid(), 'James Wilson', 42, 'Male', '202 Elm St, Phoenix, AZ', '602-555-5678', 'james.w@email.com', 'Blue', CURRENT_DATE),
(gen_random_uuid(), 'Olivia Brown', 31, 'Female', '303 Birch Dr, Philadelphia, PA', '215-555-6789', 'olivia.b@email.com', 'Black', CURRENT_DATE),
(gen_random_uuid(), 'William Taylor', 26, 'Male', '404 Maple Ave, San Antonio, TX', '210-555-7890', 'william.t@email.com', 'Green', CURRENT_DATE),
(gen_random_uuid(), 'Ava Thomas', 22, 'Female', '505 Walnut Rd, San Diego, CA', '619-555-8901', 'ava.t@email.com', 'Yellow', CURRENT_DATE),
(gen_random_uuid(), 'Benjamin Harris', 38, 'Male', '606 Spruce St, Dallas, TX', '214-555-9012', 'benjamin.h@email.com', 'White', CURRENT_DATE),
(gen_random_uuid(), 'Isabella Clark', 29, 'Female', '707 Chestnut Blvd, San Jose, CA', '408-555-0123', 'isabella.c@email.com', 'Orange', CURRENT_DATE);

