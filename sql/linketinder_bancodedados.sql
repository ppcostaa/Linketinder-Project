DROP TABLE IF EXISTS Matchs CASCADE;
DROP TABLE IF EXISTS Likes CASCADE;
DROP TABLE IF EXISTS Candidato_Competencias CASCADE;
DROP TABLE IF EXISTS Vagas CASCADE;
DROP TABLE IF EXISTS Empresas CASCADE;
DROP TABLE IF EXISTS Candidatos CASCADE;
DROP TABLE IF EXISTS Competencias CASCADE;
DROP TABLE IF EXISTS Localizacao CASCADE;
DROP TABLE IF EXISTS Usuarios CASCADE;

CREATE TABLE Usuarios (
    id_usuario SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(20) NOT NULL,
    descricao TEXT
);

CREATE TABLE Localizacao (
    id_localizacao SERIAL PRIMARY KEY,
    cep VARCHAR(10),
    pais VARCHAR(100)
);

CREATE TABLE Competencias (
    id_competencia SERIAL PRIMARY KEY,
    nome_competencia VARCHAR(255) NOT NULL
);

CREATE TABLE Candidatos (
    id_candidato SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES Usuarios(id_usuario),
    id_localizacao INT REFERENCES Localizacao(id_localizacao),
    nome VARCHAR(100),
    sobrenome VARCHAR(100),
    data_nascimento DATE,
    cpf VARCHAR(14) UNIQUE NOT NULL
);

CREATE TABLE Empresas (
    id_empresa SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES Usuarios(id_usuario),
    id_localizacao INT REFERENCES Localizacao(id_localizacao),
    nome_empresa VARCHAR(255),
    cnpj VARCHAR(18) UNIQUE NOT NULL
);

CREATE TABLE Vagas (
    id_vaga SERIAL PRIMARY KEY,
    id_empresa INT REFERENCES Empresas(id_empresa),
    nome_vaga VARCHAR(255),
    descricao_vaga TEXT,
    local_estado VARCHAR(100),
    local_cidade VARCHAR(100)
);

CREATE TABLE Candidato_Competencias (
    id_candidato INT REFERENCES Candidatos(id_candidato),
    id_competencia INT REFERENCES Competencias(id_competencia),
    PRIMARY KEY(id_candidato, id_competencia)
);

CREATE TABLE Likes (
    id_like SERIAL PRIMARY KEY,
    id_candidato INT REFERENCES Candidatos(id_candidato),
    id_empresa INT REFERENCES Empresas(id_empresa)
);

CREATE TABLE Matchs (
    candidato_id INT REFERENCES Candidatos(id_candidato) ON DELETE CASCADE,
    empresa_id INT REFERENCES Empresas(id_empresa) ON DELETE CASCADE,
    PRIMARY KEY (candidato_id, empresa_id)
);

INSERT INTO Usuarios (email, senha, descricao)
VALUES 
('sandubinha@email.com', 'senha123', 'Busco novas oportunidades de trabalho.'),
('contato@pastelsoft.com', 'senha303', 'Software ERP para redes de restaurantes.');

INSERT INTO Localizacao (cep, pais)
VALUES 
('12345-678', 'Brasil'),
('54321-123', 'Brasil');

INSERT INTO Competencias (nome_competencia) VALUES ('Python'), ('Java'), ('Groovy'), ('Angular'), ('Ilustrador');

INSERT INTO Candidatos (id_usuario, id_localizacao, nome, sobrenome, data_nascimento, cpf)
VALUES 
(1, 1, 'Sandubinha', 'Silva', '1990-05-15', '12345678900');

INSERT INTO Empresas (id_usuario, id_localizacao, nome_empresa, cnpj)
VALUES 
(2, 2, 'Pastelsoft', '12.345.678/0001-90');

INSERT INTO Vagas (id_empresa, nome_vaga, descricao_vaga, local_estado, local_cidade) 
VALUES 
(1, 'Desenvolvedor Java', 'Desenvolvimento de sistemas para ERP.', 'São Paulo', 'São Paulo');

INSERT INTO Likes (id_candidato, id_empresa) 
VALUES (1, 1);
INSERT INTO Likes (id_empresa, id_candidato) 
VALUES (1, 1);

INSERT INTO Matchs (candidato_id, empresa_id)
SELECT l.id_candidato, l.id_empresa
FROM Likes l
WHERE EXISTS (
    SELECT 1
    FROM Likes l2
    WHERE l2.id_candidato = l.id_empresa
      AND l2.id_empresa = l.id_candidato
)
AND l.id_candidato = 1
AND l.id_empresa = 1
ON CONFLICT (candidato_id, empresa_id) DO NOTHING;

SELECT * FROM Candidatos;
SELECT * FROM Empresas;
SELECT * FROM Vagas;
SELECT * FROM Candidato_Competencias;
SELECT * FROM Likes;
SELECT * FROM Competencias;
SELECT * FROM Matchs WHERE candidato_id = 1 AND empresa_id = 1;
