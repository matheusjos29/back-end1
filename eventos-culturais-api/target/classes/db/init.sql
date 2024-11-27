-- Criação das tabelas
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS categorias (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS eventos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    local VARCHAR(200) NOT NULL,
    capacidade INTEGER NOT NULL,
    categoria_id INTEGER NOT NULL REFERENCES categorias(id),
    gratuito BOOLEAN NOT NULL,
    preco DECIMAL(10,2),
    CONSTRAINT ck_eventos_preco CHECK (gratuito = true OR (preco IS NOT NULL AND preco > 0))
);

-- Inserção de dados iniciais
INSERT INTO categorias (nome, descricao) VALUES
    ('Música', 'Eventos musicais e shows'),
    ('Teatro', 'Apresentações teatrais'),
    ('Dança', 'Espetáculos de dança'),
    ('Literatura', 'Eventos literários e saraus'),
    ('Artes Visuais', 'Exposições e mostras de arte');

-- Inserção do usuário admin (senha: admin123)
INSERT INTO usuarios (email, senha, nome, role) VALUES
    ('admin@eventos.com', '$2a$10$8YFIS0nhmMcqKBFeCcNRK.XQFJoGNGHgF/PG6pU4U5nYWHQYLO3x6', 'Administrador', 'ADMIN');