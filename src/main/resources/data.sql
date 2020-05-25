INSERT INTO person (id, firstname, lastname, email, gender, dob) VALUES
('92a9af9f-90f4-4b0e-b77b-1088fa929e69','Per', 'Persson', 'per@mail.se', 'MALE', '1990-01-01'),
('3a5a01b2-215b-4322-9deb-5a4452145600','Malin', 'Malinsson', 'malin@mail.se', 'FEMALE', '1995-01-01'),
('664aa3b1-4926-4974-94b4-6e9c8d981a34','Bo', 'Bosson', 'bo@mail.se', 'MALE', '1985-01-01')
ON CONFLICT (id) DO NOTHING;