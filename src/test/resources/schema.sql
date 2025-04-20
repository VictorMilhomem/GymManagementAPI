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
