CREATE TABLE Person (
  id         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(60) NOT NULL,
  last_name  VARCHAR(40) NOT NULL,
  birth_date DATE,

  UNIQUE (first_name, last_name)
);

INSERT INTO Person (first_name, last_name, birth_date) VALUES ('Chris', 'Schaefer', '1981-05-03');
INSERT INTO Person (first_name, last_name, birth_date) VALUES ('Scott', 'Tiger', '1990-11-02');
INSERT INTO Person (first_name, last_name, birth_date) VALUES ('John', 'Smith', '1964-02-28');

CREATE TABLE User (
  id            INT PRIMARY KEY,
  email         VARCHAR(20),
  password      VARCHAR(20),

  CONSTRAINT user_from_person_inheritance FOREIGN KEY (id) REFERENCES Person(id)
);

INSERT INTO User (id, email, password) VALUES (1, 'Chris.Schaefer@gmail.com', 'qwerty1981');
INSERT INTO User (id, email, password) VALUES (2, 'Scott.Tiger@gmail.com', 'qwerty1990');
INSERT INTO User (id, email, password) VALUES (3, 'John.Smith@gmail.com', 'qwerty1964');

// todo: make stored procedure to add User comfortably!