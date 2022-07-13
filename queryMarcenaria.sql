-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: marcenaria
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `COD_CLIENTE` int NOT NULL AUTO_INCREMENT,
  `NOME` varchar(150) NOT NULL,
  `RG` varchar(15) NOT NULL,
  `CPF` varchar(15) NOT NULL,
  `RUA` varchar(150) NOT NULL,
  `NUMERO` int NOT NULL,
  `COMPLEMENTO` varchar(100) DEFAULT NULL,
  `BAIRRO` varchar(150) NOT NULL,
  `CEP` varchar(9) NOT NULL,
  `DDD` varchar(2) NOT NULL,
  `TELEFONE` varchar(10) DEFAULT NULL,
  `CELULAR` varchar(10) DEFAULT NULL,
  `EMAIL` varchar(60) DEFAULT NULL,
  `DATA_CADASTRO` datetime NOT NULL,
  `OBS` text,
  `CIDADE` varchar(100) NOT NULL,
  `ESTADO` varchar(500) NOT NULL,
  `UF` varchar(2) NOT NULL,
  PRIMARY KEY (`COD_CLIENTE`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `empresa`
--

DROP TABLE IF EXISTS `empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresa` (
  `COD_EMPRESA` int NOT NULL AUTO_INCREMENT,
  `RAZAO_SOCIAL` varchar(200) DEFAULT NULL,
  `NOME_FANTASIA` varchar(200) NOT NULL,
  `CNPJ` varchar(50) NOT NULL,
  `ATIVIDADE_FIM` varchar(50) NOT NULL,
  `RUA` varchar(100) NOT NULL,
  `NUMERO` int NOT NULL,
  `COMPLEMENTO` varchar(100) DEFAULT NULL,
  `BAIRRO` varchar(150) NOT NULL,
  `CEP` varchar(9) NOT NULL,
  `DDD` varchar(2) NOT NULL,
  `TELEFONE` varchar(10) NOT NULL,
  `SITE` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `OBS` text,
  `CIDADE` varchar(100) NOT NULL,
  `ESTADO` varchar(50) NOT NULL,
  `UF` varchar(2) NOT NULL,
  PRIMARY KEY (`COD_EMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entrada_produto`
--

DROP TABLE IF EXISTS `entrada_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrada_produto` (
  `COD_ENTRADA` int NOT NULL AUTO_INCREMENT,
  `NUMERO_NF` varchar(15) NOT NULL,
  `COD_PRODUTO` int NOT NULL,
  `DATA_ENTRADA` datetime NOT NULL,
  `QUANTIDADE` int NOT NULL,
  `VALOR_UNIT` decimal(10,2) NOT NULL,
  `VALOR_TOTAL` decimal(10,2) NOT NULL,
  `VALOR_TOTAL_NOTA` decimal(10,2) NOT NULL,
  PRIMARY KEY (`COD_ENTRADA`),
  KEY `FK_PRODUTO_2_PRODUTO` (`COD_PRODUTO`),
  CONSTRAINT `FK_PRODUTO_2_PRODUTO` FOREIGN KEY (`COD_PRODUTO`) REFERENCES `produto` (`COD_PRODUTO`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estoque`
--

DROP TABLE IF EXISTS `estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estoque` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `COD_PRODUTO` int NOT NULL,
  `ESTOQUE_ATUAL` int NOT NULL,
  `ESTOQUE_MINIMO` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COD_PRODUTO` (`COD_PRODUTO`),
  CONSTRAINT `FK_ESTOQUE_2_PRODUTO` FOREIGN KEY (`COD_PRODUTO`) REFERENCES `produto` (`COD_PRODUTO`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fornecedor`
--

DROP TABLE IF EXISTS `fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fornecedor` (
  `COD_FORNECEDOR` int NOT NULL AUTO_INCREMENT,
  `RAZAO_SOCIAL` varchar(200) DEFAULT NULL,
  `NOME_FANTASIA` varchar(200) NOT NULL,
  `CNPJ` varchar(50) NOT NULL,
  `ATIVIDADE_FIM` varchar(50) NOT NULL,
  `RUA` varchar(100) NOT NULL,
  `NUMERO` int NOT NULL,
  `COMPLEMENTO` varchar(100) DEFAULT NULL,
  `BAIRRO` varchar(150) NOT NULL,
  `CEP` varchar(9) NOT NULL,
  `DDD` varchar(2) NOT NULL,
  `TELEFONE` varchar(10) NOT NULL,
  `SITE` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `OBS` text,
  `CIDADE` varchar(100) NOT NULL,
  `ESTADO` varchar(50) NOT NULL,
  `UF` varchar(2) NOT NULL,
  PRIMARY KEY (`COD_FORNECEDOR`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionario` (
  `REGISTRO_FUNC` int NOT NULL AUTO_INCREMENT,
  `NOME` varchar(150) NOT NULL,
  `RG` varchar(15) NOT NULL,
  `CPF` varchar(15) NOT NULL,
  `CTPS` varchar(15) DEFAULT NULL,
  `RUA` varchar(150) NOT NULL,
  `NUMERO` int NOT NULL,
  `COMPLEMENTO` varchar(100) DEFAULT NULL,
  `BAIRRO` varchar(150) NOT NULL,
  `CEP` varchar(9) NOT NULL,
  `DDD` varchar(2) NOT NULL,
  `TELEFONE` varchar(10) DEFAULT NULL,
  `CELULAR` varchar(10) DEFAULT NULL,
  `DATA_NASC` datetime NOT NULL,
  `DATA_ADMISSAO` datetime NOT NULL,
  `TIPO_SANG` varchar(3) NOT NULL,
  `FUNCAO` varchar(50) NOT NULL,
  `SETOR` varchar(50) DEFAULT NULL,
  `SALARIO` decimal(10,2) NOT NULL,
  `OBS` text,
  `CIDADE` varchar(100) NOT NULL,
  `ESTADO` varchar(50) NOT NULL,
  `UF` varchar(2) NOT NULL,
  PRIMARY KEY (`REGISTRO_FUNC`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_compra_material`
--

DROP TABLE IF EXISTS `nota_compra_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nota_compra_material` (
  `COD_NOTA` int NOT NULL AUTO_INCREMENT,
  `COD_FORNECEDOR` int NOT NULL,
  `NUMERO_NF` varchar(15) NOT NULL,
  `COD_PRODUTO` int NOT NULL,
  `QUANTIDADE` int NOT NULL,
  `VALOR_UNIT` decimal(10,2) NOT NULL,
  `VALOR_TOTAL` decimal(10,2) NOT NULL,
  `VALOR_TOTAL_NOTA` decimal(10,2) NOT NULL,
  `VALOR_DESCONTO` decimal(10,2) DEFAULT NULL,
  `CHAVE_NF` varchar(50) NOT NULL,
  `DATA_EMISSAO` datetime NOT NULL,
  `OBS` text,
  `DATA_ENTRADA` datetime NOT NULL,
  PRIMARY KEY (`COD_NOTA`,`NUMERO_NF`),
  KEY `FK_NOTA_COMPRA_2_FORNECEDOR` (`COD_FORNECEDOR`),
  KEY `FK_NOTA_COMPRA_2_PRODUTO` (`COD_PRODUTO`),
  KEY `VALOR_TOTAL` (`VALOR_TOTAL`),
  KEY `VALOR_TOTAL_NOTA` (`VALOR_TOTAL_NOTA`),
  KEY `NUMERO_NF` (`NUMERO_NF`),
  KEY `VALOR_UNIT` (`VALOR_UNIT`),
  CONSTRAINT `FK_NOTA_COMPRA_2_FORNECEDOR` FOREIGN KEY (`COD_FORNECEDOR`) REFERENCES `fornecedor` (`COD_FORNECEDOR`),
  CONSTRAINT `FK_NOTA_COMPRA_2_PRODUTO` FOREIGN KEY (`COD_PRODUTO`) REFERENCES `produto` (`COD_PRODUTO`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orcamento_cliente`
--

DROP TABLE IF EXISTS `orcamento_cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orcamento_cliente` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NUM_ORCAMENTO` int NOT NULL,
  `COD_CLIENTE` int NOT NULL,
  `TELEFONE` varchar(15) DEFAULT NULL,
  `CELULAR` varchar(15) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `DESC_SERVICO` text NOT NULL,
  `DATA_ORCAMENTO` datetime NOT NULL,
  `COD_PRODUTO` int NOT NULL,
  `QUANTIDADE` int NOT NULL,
  `VALOR` decimal(10,2) DEFAULT NULL,
  `VALOR_TOTAL` decimal(10,2) DEFAULT NULL,
  `VALOR_QUAD` decimal(10,2) DEFAULT NULL,
  `VALOR_OBRA` decimal(10,2) DEFAULT NULL,
  `OBS` text,
  PRIMARY KEY (`ID`),
  KEY `FK_COD_CLIENTE_2_CLIENTE` (`COD_CLIENTE`),
  KEY `FK_PRODUTO_2_PROD` (`COD_PRODUTO`),
  CONSTRAINT `FK_COD_CLIENTE_2_CLIENTE` FOREIGN KEY (`COD_CLIENTE`) REFERENCES `cliente` (`COD_CLIENTE`),
  CONSTRAINT `FK_PRODUTO_2_PROD` FOREIGN KEY (`COD_PRODUTO`) REFERENCES `produto` (`COD_PRODUTO`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orcamento_empresa`
--

DROP TABLE IF EXISTS `orcamento_empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orcamento_empresa` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NUM_ORCAMENTO` int NOT NULL,
  `COD_EMPRESA` int NOT NULL,
  `NOME_RESPONSAVEL` varchar(150) NOT NULL,
  `TELEFONE` varchar(15) DEFAULT NULL,
  `CELULAR` varchar(15) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `DESC_SERVICO` text NOT NULL,
  `DATA_ORCAMENTO` datetime NOT NULL,
  `COD_PRODUTO` int NOT NULL,
  `QUANTIDADE` int NOT NULL,
  `VALOR` decimal(10,2) DEFAULT NULL,
  `VALOR_TOTAL` decimal(10,2) DEFAULT NULL,
  `VALOR_QUAD` decimal(10,2) DEFAULT NULL,
  `VALOR_OBRA` decimal(10,2) DEFAULT NULL,
  `OBS` text,
  PRIMARY KEY (`ID`),
  KEY `FK_COD_EMPRESA_2_EMPRESA` (`COD_EMPRESA`),
  KEY `FK_PRODUTO_2_PROD2` (`COD_PRODUTO`),
  CONSTRAINT `FK_COD_EMPRESA_2_EMPRESA` FOREIGN KEY (`COD_EMPRESA`) REFERENCES `empresa` (`COD_EMPRESA`),
  CONSTRAINT `FK_PRODUTO_2_PROD2` FOREIGN KEY (`COD_PRODUTO`) REFERENCES `produto` (`COD_PRODUTO`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ordem_servico_cliente`
--

DROP TABLE IF EXISTS `ordem_servico_cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordem_servico_cliente` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NUM_PEDIDO` int NOT NULL,
  `COD_CLIENTE` int NOT NULL,
  `DESC_SERVICO` varchar(255) NOT NULL,
  `DATA_ORDEM` datetime NOT NULL,
  `DATA_INICIO` datetime DEFAULT NULL,
  `PRAZO_ENTREGA` datetime DEFAULT NULL,
  `DATA_ENTREGA` datetime DEFAULT NULL,
  `STATUS_SERVICO` varchar(20) NOT NULL,
  `VALOR_TOTAL` decimal(10,2) NOT NULL,
  `REGISTRO_FUNC` int NOT NULL,
  `OBS` text,
  PRIMARY KEY (`ID`),
  KEY `FK_ORDEM_SERVICO_2_CLIENT` (`COD_CLIENTE`),
  KEY `FK_ORDEM_SERVICO_2_FUNCIONARIO` (`REGISTRO_FUNC`),
  CONSTRAINT `FK_ORDEM_SERVICO_2_CLIENT` FOREIGN KEY (`COD_CLIENTE`) REFERENCES `cliente` (`COD_CLIENTE`),
  CONSTRAINT `FK_ORDEM_SERVICO_2_FUNCIONARIO` FOREIGN KEY (`REGISTRO_FUNC`) REFERENCES `funcionario` (`REGISTRO_FUNC`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ordem_servico_empresa`
--

DROP TABLE IF EXISTS `ordem_servico_empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordem_servico_empresa` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NUM_PEDIDO` int NOT NULL,
  `COD_EMPRESA` int NOT NULL,
  `NOME_RESPONSAVEL` varchar(150) NOT NULL,
  `DESC_SERVICO` varchar(255) NOT NULL,
  `DATA_ORDEM` datetime NOT NULL,
  `DATA_INICIO` datetime DEFAULT NULL,
  `PRAZO_ENTREGA` datetime DEFAULT NULL,
  `DATA_ENTREGA` datetime DEFAULT NULL,
  `STATUS_SERVICO` varchar(30) DEFAULT NULL,
  `VALOR_TOTAL` decimal(10,2) NOT NULL,
  `REGISTRO_FUNC` int NOT NULL,
  `OBS` text,
  PRIMARY KEY (`ID`),
  KEY `FK_ORDEM_SERVICO_2_EMPRESA` (`COD_EMPRESA`),
  KEY `FK_ORDEM_SERVICO_2_FUNCIONARIO_E` (`REGISTRO_FUNC`),
  CONSTRAINT `FK_ORDEM_SERVICO_2_EMPRESA` FOREIGN KEY (`COD_EMPRESA`) REFERENCES `empresa` (`COD_EMPRESA`),
  CONSTRAINT `FK_ORDEM_SERVICO_2_FUNCIONARIO_E` FOREIGN KEY (`REGISTRO_FUNC`) REFERENCES `funcionario` (`REGISTRO_FUNC`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produto` (
  `COD_PRODUTO` int NOT NULL AUTO_INCREMENT,
  `DESC_PRODUTO` varchar(250) NOT NULL,
  `PRECO_UNIT` decimal(10,2) NOT NULL,
  PRIMARY KEY (`COD_PRODUTO`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `saida_produto`
--

DROP TABLE IF EXISTS `saida_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `saida_produto` (
  `COD_SAIDA` int NOT NULL AUTO_INCREMENT,
  `ID_ESTOQUE` int NOT NULL,
  `COD_PRODUTO` int NOT NULL,
  `DATA_SAIDA` datetime NOT NULL,
  `QUANTIDADE` int NOT NULL,
  `RESP_SAIDA` int NOT NULL,
  PRIMARY KEY (`COD_SAIDA`),
  KEY `FK_SAIDA_PRODUTO_2_ID_ESTOQUE` (`ID_ESTOQUE`),
  KEY `FK_SAIDA_PRODUTO_2_ESTOQUE` (`COD_PRODUTO`),
  KEY `FK_SAIDA_PRODUTO_2_FUNCIONARIO` (`RESP_SAIDA`),
  CONSTRAINT `FK_SAIDA_PRODUTO_2_ESTOQUE` FOREIGN KEY (`COD_PRODUTO`) REFERENCES `estoque` (`COD_PRODUTO`),
  CONSTRAINT `FK_SAIDA_PRODUTO_2_FUNCIONARIO` FOREIGN KEY (`RESP_SAIDA`) REFERENCES `funcionario` (`REGISTRO_FUNC`),
  CONSTRAINT `FK_SAIDA_PRODUTO_2_ID_ESTOQUE` FOREIGN KEY (`ID_ESTOQUE`) REFERENCES `estoque` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-13 14:12:12
