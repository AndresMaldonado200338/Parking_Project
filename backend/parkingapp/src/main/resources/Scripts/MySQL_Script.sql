-- LoginDB
CREATE DATABASE IF NOT EXISTS LoginDB;

USE LoginDB;

CREATE TABLE
    Login (
        id SERIAL AUTO_INCREMENT NOT NULL PRIMARY KEY,
        userID BIGINT NOT NULL,
        password VARCHAR(20) NOT NULL
    );

-- GestionEmpleadosDB
CREATE DATABASE IF NOT EXISTS GestionEmpleadosDB;

USE GestionEmpleadosDB;

CREATE TABLE
    IF NOT EXISTS Employee (
        document VARCHAR(50),
        firstname VARCHAR(50),
        lastname VARCHAR(50),
        email VARCHAR(100),
        phone VARCHAR(20),
        status BOOLEAN,
        PRIMARY KEY (document)
    );

CREATE TABLE
    IF NOT EXISTS Access (
        employeeID VARCHAR(50),
        accessdatetime DATETIME,
        FOREIGN KEY (employeeID) REFERENCES Employee (document)
    );