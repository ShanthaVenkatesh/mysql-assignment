-- 1. STUDENTS TABLE DATA

INSERT INTO students (name, email, phone) VALUES
('Rakshitha', 'rakshitha@gmail.com', '9876543210'),
('Artha', 'artha@gmail.com', '9876543211'),
('Sita', 'sita@gmail.com', '9876543212'),
('Riya', 'riya@gmail.com', '9876543213'),
('Priya', 'priya@gmail.com', '9876543214'),
('Anu', 'anu@gmail.com', '9876543215');

-- 2. COURSES TABLE

INSERT INTO courses (course_name, course_duration) VALUES
('Java Full Stack', '6 Months'),
('Python Full Stack', '6 Months'),
('Data Science', '8 Months'),
('Web Development', '4 Months'),
('SQL and Database', '3 Months'),
('Machine Learning', '6 Months');

-- 3. ENROLLMENTS TABLE DATA

INSERT INTO enrollments
(student_id, course_id, enrollment_date) VALUES
(1, 1, '2026-01-10'),
(2, 2, '2026-01-12'),
(3, 3, '2026-01-15'),
(4, 4, '2026-01-18'),
(5, 5, '2026-01-20'),
(6, 6, '2026-01-22');

-- 4. ATTENDANCE TABLE DATA

INSERT INTO attendance
(student_id, course_id, total_classes, attended_classes) VALUES
(1, 1, 50, 45),
(2, 2, 60, 52),
(3, 3, 70, 63),
(4, 4, 40, 35),
(5, 5, 30, 27),
(6, 6, 55, 48);


-- 5. MARKS TABLE 

INSERT INTO marks
(student_id, course_id, total_marks, obtained_marks) VALUES
(1, 1, 100, 85),
(2, 2, 100, 78),
(3, 3, 100, 92),
(4, 4, 100, 74),
(5, 5, 100, 88),
(6, 6, 100, 81);