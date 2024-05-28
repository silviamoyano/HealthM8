CREATE DATABASE  IF NOT EXISTS `healthm8` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish2_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `healthm8`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: healthm8
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citas` (
  `id_cita` int NOT NULL AUTO_INCREMENT,
  `fecha_cita` date DEFAULT NULL,
  `hora_cita` time DEFAULT NULL,
  `lugar_cita` varchar(120) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `es_online` int DEFAULT NULL,
  `es_telefonica` int DEFAULT NULL,
  `nombre_medico` varchar(50) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `id_usuariofk` int DEFAULT NULL,
  `id_especialidadfk` int DEFAULT NULL,
  PRIMARY KEY (`id_cita`),
  KEY `id_usuariofk_idx` (`id_usuariofk`),
  KEY `id_especialidadfk_idx` (`id_especialidadfk`),
  CONSTRAINT `id_especialidadfk2` FOREIGN KEY (`id_especialidadfk`) REFERENCES `especialidades` (`id_especialidad`),
  CONSTRAINT `id_usuariofk1` FOREIGN KEY (`id_usuariofk`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `citas`
--

LOCK TABLES `citas` WRITE;
/*!40000 ALTER TABLE `citas` DISABLE KEYS */;
INSERT INTO `citas` VALUES (1,'2024-06-17','18:30:00','Clínica Sanitas',0,0,'Samara',1,32),(3,'2024-05-28','19:15:00','Centro de salud',0,0,'',1,2),(4,'2024-07-20','09:00:00','Centro de salud',0,0,'',1,2),(5,'2024-06-03','08:30:00','Centro de Salud',0,0,'',3,2);
/*!40000 ALTER TABLE `citas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `especialidades`
--

DROP TABLE IF EXISTS `especialidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `especialidades` (
  `id_especialidad` int NOT NULL AUTO_INCREMENT,
  `nombre_especialidad` varchar(100) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`id_especialidad`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `especialidades`
--

LOCK TABLES `especialidades` WRITE;
/*!40000 ALTER TABLE `especialidades` DISABLE KEYS */;
INSERT INTO `especialidades` VALUES (1,'Alergología'),(2,'Análisis clínicos'),(3,'Anestesiología y tratamiento del dolor'),(4,'Angiología y Cirugía Vascular'),(5,'Aparato Digestivo'),(6,'Cardiología'),(7,'Cirugía Cardiovascular'),(8,'Cirugía General y Digestiva'),(9,'Cirugía Oral Maxilofacial'),(10,'Cirugía Pediátrica'),(11,'Cirugía Reparadora'),(12,'Cirugía Torácica'),(13,'Dentista'),(14,'Dermatología'),(15,'Dietética y Nutrición'),(16,'Endocrinología'),(17,'Enfermería'),(18,'Fisioterapia'),(19,'Geriatría'),(20,'Hematología'),(21,'Logofoniatría'),(22,'Logopedia'),(23,'Medicina Educación Física y del Deporte'),(24,'Medicina General'),(25,'Medicina Interna'),(26,'Nefrología'),(27,'Neumología'),(28,'Neurocirugía'),(29,'Neurofisiología Clínica'),(30,'Neurología'),(31,'Obstetricia y Ginecología'),(32,'Odontología'),(33,'Oftalmología'),(34,'Oncología Médica'),(35,'Otorrinolaringología'),(36,'Pediatría y Áreas Específicas'),(37,'Podología'),(38,'Psicología'),(39,'Psiquiatría'),(40,'Puericultura'),(41,'Radiodiagnóstico'),(42,'Rehabilitación'),(43,'Reumatología'),(44,'Traumatología'),(45,'Unidad Genética Clínica'),(46,'Urología'),(47,'Otros');
/*!40000 ALTER TABLE `especialidades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos`
--

DROP TABLE IF EXISTS `medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos` (
  `id_medicamento` int NOT NULL AUTO_INCREMENT,
  `nombre_medicamento` varchar(100) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `toma_medicamento` int DEFAULT NULL,
  `numero_pastillas` int DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_renovacionreceta` date DEFAULT NULL,
  `id_usuariofk` int DEFAULT NULL,
  PRIMARY KEY (`id_medicamento`),
  KEY `id_usuariofk3_idx` (`id_usuariofk`),
  CONSTRAINT `id_usuariofk3` FOREIGN KEY (`id_usuariofk`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos`
--

LOCK TABLES `medicamentos` WRITE;
/*!40000 ALTER TABLE `medicamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(50) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `clave_usuario` varchar(100) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Silvia','123.S'),(3,'SilviaMoyano','123.S');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-28 19:36:42
