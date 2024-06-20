drop database if exists CSEDU_BANK;
create database CSEDU_BANK;
use CSEDU_BANk;
create table admin (
  username varchar(255) primary key,
    password varchar(255)
);
create table customer (
  username varchar(255) primary key,
    password varchar(255)
);

create table adminall (
  username varchar(255) primary key,
    fname varchar(255),
    lname varchar(255),
    phoneno varchar(255),
    email varchar(255),
    dob date,
    gender varchar(255)
);
create table customerall (
  username varchar(255) primary key,
    fname varchar(255),
    lname varchar(255),
    phoneno varchar(255),
    email varchar(255),
    dob date,
    Gender ENUM('Male', 'Female', 'Other'),
    balance float
);

create table transactions (
  ID INT AUTO_INCREMENT PRIMARY KEY,
    accountNumber int,
    customername varchar(255),
    transactiontype varchar(255),
    amount int,
    toaccountnumber int
);
create table useraccounts(
  username varchar(255),
    accountnumber int auto_increment primary key,
    balance int
);
create table advance(
  username varchar(255),
    accountnumber int primary key,
    balance int,
    accounttype varchar(255),
    startingdate date,
    endingdate date
);