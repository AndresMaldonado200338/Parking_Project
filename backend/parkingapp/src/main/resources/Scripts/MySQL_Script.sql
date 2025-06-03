-- GestionEmpleadosDB
CREATE DATABASE IF NOT EXISTS GestionEmpleadosDB;

USE GestionEmpleadosDB;

CREATE TABLE
    IF NOT EXISTS employee (
        document VARCHAR(50),
        firstname VARCHAR(50),
        lastname VARCHAR(50),
        email VARCHAR(100),
        phone VARCHAR(20),
        status BOOLEAN,
        PRIMARY KEY (document)
    );

CREATE TABLE
    IF NOT EXISTS access (
        id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- Added this ID column
        employeeID VARCHAR(50),
        accessdatetime DATETIME,
        event_type VARCHAR(10) NOT NULL DEFAULT 'ENTRADA'
            CHECK (event_type IN ('ENTRADA', 'SALIDA')),
        FOREIGN KEY (employeeID) REFERENCES employee (document)
    );