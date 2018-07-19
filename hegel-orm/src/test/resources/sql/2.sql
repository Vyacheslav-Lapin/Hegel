CREATE TABLE User (
  id            INT PRIMARY KEY,
  email         VARCHAR(100),
  password      VARCHAR(20),

  CONSTRAINT user_from_person_inheritance FOREIGN KEY (id) REFERENCES Person(id)
);

INSERT INTO User (id, email, password) VALUES (1, 'Chris.Schaefer@gmail.com', 'qwerty1981');
INSERT INTO User (id, email, password) VALUES (2, 'Scott.Tiger@gmail.com', 'qwerty1990');
INSERT INTO User (id, email, password) VALUES (3, 'John.Smith@gmail.com', 'qwerty1964');

-- todo: make stored procedure to add User comfortably!