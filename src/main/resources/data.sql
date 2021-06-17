CREATE TABLE account(
    id VARCHAR2(255) PRIMARY KEY ,
    password VARCHAR2(8)
);
CREATE TABLE url(
    id UUID PRIMARY KEY,
    longUrl VARCHAR2(255),
    shortUrl VARCHAR2(255),
    redirectType TINYINT,
    calls INT8
);