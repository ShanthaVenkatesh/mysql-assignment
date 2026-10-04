 -- Employee data
 
INSERT INTO employees(employee_name, email, phone)VALUES
('Rakshitha', 'rakshitha@gmail.com', '9876543210'),
('Artha', 'artha@gmail.com', '9876543211'),
('Sita', 'sita@gmail.com', '9876543212'),
('Riya', 'riya@gmail.com', '9876543213'),
('Priya', 'priya@gmail.com', '9876543214');

-- leave_requests data 

INSERT INTO leave_requests (employee_id, employee_name, leave_type, start_date, end_date, reason, leave_status) VALUES
(1, 'Rakshitha', 'Casual Leave', '2026-10-05', '2026-10-06', 'Personal work', 'Pending'),
(2, 'Artha', 'Sick Leave', '2026-10-07', '2026-10-08', 'Not feeling well', 'Approved'),
(3, 'Sita', 'Earned Leave', '2026-10-10', '2026-10-12', 'Family function', 'Pending'),
(4, 'Riya', 'Emergency Leave', '2026-10-15', '2026-10-16', 'Family emergency', 'Rejected'),
(5, 'Priya', 'Vacation Leave', '2026-10-20', '2026-10-22', 'Holiday trip', 'Approved');

-- leave_balance data

INSERT INTO leave_balance (employee_id, total_leaves, used_leaves, remaining_leaves) VALUES
(1, 20, 2, 18),
(2, 20, 1, 19),
(3, 20, 3, 17),
(4, 20, 4, 16),
(5, 20, 6, 14);