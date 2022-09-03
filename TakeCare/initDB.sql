/*create user and grand sufficient privilages*/
CREATE USER 'carbocation'@'localhost' IDENTIFIED BY '8bitguy';
GRANT ALL PRIVILEGES ON *.* TO 'carbocation'@'localhost';

CREATE DATABASE TakeCareDB;

