USE healthm8;

-- Creamos un usuario para gestionar la BD
CREATE USER 'api_healthm8' @'localhost' IDENTIFIED BY 'HealthMate8;';
GRANT ALL ON healthm8.* TO 'api_healthm8' @'localhost';
FLUSH PRIVILEGES;

-- INSERT de Especialidades
INSERT INTO especialidades VALUES 
(null, 'Análisis clínicos'
),(
null, 'Anestesiología y tratamiento del dolor'),(
null, 'Angiología y Cirugía Vascular'),(
null, 'Aparato Digestivo'),(
null, 'Cardiología'),(
null, 'Cirugía Cardiovascular'),(
null, 'Cirugía General y Digestiva'),(
null, 'Cirugía Oral Maxilofacial'),(
null, 'Cirugía Pediátrica'),(
null, 'Cirugía Reparadora'),(
null, 'Cirugía Torácica'),(
null, 'Dentista'),(
null, 'Dermatología'),(
null, 'Dietética y Nutrición'),(
null, 'Endocrinología'),(
null, 'Enfermería'),(
null, 'Fisioterapia'),(
null, 'Geriatría'),(
null, 'Hematología'),(
null, 'Logofoniatría'),(
null, 'Logopedia'),(
null, 'Medicina Educación Física y del Deporte'),(
null, 'Medicina General'),(
null, 'Medicina Interna'),(
null, 'Nefrología'),(
null, 'Neumología'),(
null, 'Neurocirugía'),(
null, 'Neurofisiología Clínica'),(
null, 'Neurología'),(
null, 'Obstetricia y Ginecología'),(
null, 'Odontología'),(
null, 'Oftalmología'),(
null, 'Oncología Médica'),(
null, 'Otorrinolaringología'),(
null, 'Pediatría y Áreas Específicas'),(
null, 'Podología'),(
null, 'Psicología'),(
null, 'Psiquiatría'),(
null, 'Puericultura'),(
null, 'Radiodiagnóstico'),(
null, 'Rehabilitación'),(
null, 'Reumatología'),(
null, 'Traumatología'),(
null, 'Unidad Genética Clínica'),(
null, 'Urología'),(
null, 'Otros');