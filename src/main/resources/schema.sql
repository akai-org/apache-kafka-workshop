CREATE TABLE item
(
    id IDENTITY PRIMARY KEY,
    uuid   UUID UNIQUE  NOT NULL,
    input   VARCHAR(255) NOT NULL,
    status VARCHAR(50)  NOT NULL,
    result BIGINT NULL
);

