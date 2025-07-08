
DROP DATABASE IF EXISTS dbClinica;

CREATE DATABASE dbClinica;

USE dbClinica;

-- ESTRUTURA DAS TABELAS

CREATE TABLE tb_especialidades (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT
);

CREATE TABLE tb_procedimentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    especialidade_id INT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    duracao_minutos INT NOT NULL,
    valor DECIMAL(10, 2),
    instrucoes_preparo TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (especialidade_id) REFERENCES tb_especialidades(id)
);

CREATE TABLE tb_usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL, 
    funcao ENUM('MEDICO', 'RECEPCIONISTA', 'ADMIN') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_funcionarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT UNIQUE NOT NULL,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    data_admissao DATE,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id)
);

CREATE TABLE tb_medicos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    funcionario_id INT UNIQUE NOT NULL,
    crm VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (funcionario_id) REFERENCES tb_funcionarios(id)
);

CREATE TABLE tb_medico_especialidades (
    medico_id INT NOT NULL,
    especialidade_id INT NOT NULL,
    PRIMARY KEY (medico_id, especialidade_id),
    FOREIGN KEY (medico_id) REFERENCES tb_medicos(id),
    FOREIGN KEY (especialidade_id) REFERENCES tb_especialidades(id)
);

CREATE TABLE tb_horarios_trabalho (
    id INT PRIMARY KEY AUTO_INCREMENT,
    medico_id INT NOT NULL,
    dia_semana ENUM('DOMINGO', 'SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    UNIQUE(medico_id, dia_semana, hora_inicio, hora_fim),
    FOREIGN KEY (medico_id) REFERENCES tb_medicos(id)
);

CREATE TABLE tb_pacientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    data_nascimento DATE,
    telefone VARCHAR(20) NOT NULL,
    endereco TEXT,
    convenio VARCHAR(100),
    numero_carteirinha VARCHAR(100)
);

CREATE TABLE tb_agendamentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    procedimento_id INT NOT NULL,
    data_hora_inicio DATETIME NOT NULL,
    data_hora_fim DATETIME NOT NULL,
    status ENUM('AGENDADO', 'CONCLUIDO', 'CANCELADO_PACIENTE', 'CANCELADO_CLINICA', 'NAO_COMPARECEU') NOT NULL DEFAULT 'AGENDADO',
    observacoes TEXT,
    criado_por_usuario_id INT NOT NULL, -- ID do funcionário que registrou
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES tb_pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES tb_medicos(id),
    FOREIGN KEY (procedimento_id) REFERENCES tb_procedimentos(id),
    FOREIGN KEY (criado_por_usuario_id) REFERENCES tb_usuarios(id)
);

CREATE TABLE tb_prontuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    agendamento_id INT UNIQUE NOT NULL,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    dados_triagem TEXT,
    sintomas TEXT,
    diagnostico TEXT,
    prescricao TEXT,
    solicitacao_exames TEXT,
    atestado_medico TEXT,
    data_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agendamento_id) REFERENCES tb_agendamentos(id),
    FOREIGN KEY (paciente_id) REFERENCES tb_pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES tb_medicos(id)
);


-- INSERÇÃO DE DADOS DE EXEMPLO

INSERT INTO tb_especialidades (nome, descricao) VALUES
('Cardiologia', 'Cuida de doenças do coração e do sistema circulatório.'),
('Dermatologia', 'Trata de doenças da pele, cabelos e unhas.'),
('Ortopedia', 'Especialidade que cuida de lesões e doenças do sistema locomotor.'),
('Pediatria', 'Cuida da saúde de crianças e adolescentes.'),
('Ginecologia', 'Especialidade voltada para a saúde do sistema reprodutor feminino.');

INSERT INTO tb_usuarios (email, senha, funcao, ativo) VALUES
-- Médicos
('dr.rodrigo.mendes@email.com', 'senhaMed1', 'MEDICO', TRUE),      -- ID 1
('dra.beatriz.gomes@email.com', 'senhaMed2', 'MEDICO', TRUE),      -- ID 2
('dr.lucas.ferreira@email.com', 'senhaMed3', 'MEDICO', TRUE),      -- ID 3
('dra.carla.souza@email.com', 'senhaMed4', 'MEDICO', TRUE),        -- ID 4
('dr.marcelo.oliveira@email.com', 'senhaMed5', 'MEDICO', TRUE),  -- ID 5
-- Recepcionistas
('recep.joao@email.com', 'senhaRec1', 'RECEPCIONISTA', TRUE),      -- ID 6
('recep.maria@email.com', 'senhaRec2', 'RECEPCIONISTA', TRUE),     -- ID 7
-- Admin
('admin.geral@email.com', 'senhaAdm1', 'ADMIN', TRUE),            -- ID 8
('ti.suporte@email.com', 'senhaAdm2', 'ADMIN', TRUE),             -- ID 9
('financeiro.chefe@email.com', 'senhaAdm3', 'ADMIN', TRUE);       -- ID 10

INSERT INTO tb_funcionarios (usuario_id, nome_completo, cpf, telefone, data_admissao, ativo) VALUES
(1, 'Rodrigo Mendes', '123.456.789-01', '(11) 91111-1111', '2020-01-10', TRUE), -- ID 1
(2, 'Beatriz Gomes', '234.567.890-12', '(11) 92222-2222', '2018-05-22', TRUE),  -- ID 2
(3, 'Lucas Ferreira', '345.678.901-23', '(11) 93333-3333', '2021-03-15', TRUE), -- ID 3
(4, 'Carla Souza', '456.789.012-34', '(11) 94444-4444', '2019-08-01', TRUE),    -- ID 4
(5, 'Marcelo Oliveira', '567.890.123-45', '(11) 95555-5555', '2022-02-20', TRUE),-- ID 5
(6, 'João da Silva', '678.901.234-56', '(11) 96666-6666', '2017-11-01', TRUE),   -- ID 6
(7, 'Maria Oliveira', '789.012.345-67', '(11) 97777-7777', '2023-01-30', TRUE);  -- ID 7

INSERT INTO tb_medicos (funcionario_id, crm) VALUES
(1, 'SP/123456'), 
(2, 'SP/234567'), 
(3, 'SP/345678'), 
(4, 'SP/456789'), 
(5, 'SP/567890'); 

INSERT INTO tb_medico_especialidades (medico_id, especialidade_id) VALUES
(1, 1), 
(2, 2), 
(3, 3), 
(4, 5), 
(4, 2), 
(5, 4); 

INSERT INTO tb_pacientes (nome_completo, cpf, data_nascimento, telefone, endereco, convenio, numero_carteirinha) VALUES
('Carlos Santos', '111.222.333-44', '1985-05-20', '(11) 98877-6655', 'Rua das Flores, 123, São Paulo, SP', 'Amil', '123456789'), -- ID 1
('Ana Pereira', '222.333.444-55', '1992-11-30', '(21) 99988-7766', 'Avenida Copacabana, 456, Rio de Janeiro, RJ', 'Bradesco Saúde', '987654321'), -- ID 2
('Bruno Costa', '333.444.555-66', '1978-01-15', '(31) 98765-4321', 'Rua da Bahia, 789, Belo Horizonte, MG', 'Unimed', 'A1B2C3D4E5'), -- ID 3
('Fernanda Lima', '444.555.666-77', '2001-07-10', '(51) 99123-4567', 'Avenida Ipiranga, 101, Porto Alegre, RS', 'SulAmérica', 'F6G7H8I9J0'), -- ID 4
('Juliana Alves', '555.666.777-88', '1995-03-25', '(71) 98888-9999', 'Rua do Carmo, 202, Salvador, BA', 'Amil', 'K1L2M3N4P5'); -- ID 5

INSERT INTO tb_procedimentos (especialidade_id, nome, duracao_minutos, valor, instrucoes_preparo, ativo) VALUES
(1, 'Consulta Cardiológica', 45, 350.00, 'Trazer exames anteriores, se houver.', TRUE),
(2, 'Consulta Dermatológica', 30, 300.00, 'Não usar maquiagem ou cremes na área a ser examinada.', TRUE),
(3, 'Consulta Ortopédica - Retorno', 20, 200.00, 'Trazer exames de imagem recentes.', TRUE),
(5, 'Consulta Ginecológica de Rotina', 40, 320.00, 'Abstinência sexual de 48h antes da consulta.', TRUE),
(4, 'Consulta Pediátrica', 50, 280.00, 'Trazer a carteira de vacinação da criança.', TRUE);

INSERT INTO tb_horarios_trabalho (medico_id, dia_semana, hora_inicio, hora_fim) VALUES
(1, 'SEGUNDA', '08:00:00', '12:00:00'),
(1, 'QUARTA', '14:00:00', '18:00:00'),
(2, 'TERCA', '09:00:00', '17:00:00'),
(2, 'QUINTA', '09:00:00', '17:00:00'),
(3, 'SEXTA', '08:00:00', '15:00:00');

INSERT INTO tb_agendamentos (paciente_id, medico_id, procedimento_id, data_hora_inicio, data_hora_fim, status, observacoes, criado_por_usuario_id) VALUES
(1, 1, 1, '2025-07-21 10:00:00', '2025-07-21 10:45:00', 'AGENDADO', 'Paciente ligou para confirmar.', 6), -- Criado por Recep. João
(2, 2, 2, '2025-07-22 11:00:00', '2025-07-22 11:30:00', 'AGENDADO', 'Primeira consulta.', 7), -- Criado por Recep. Maria
(3, 3, 3, '2025-07-25 09:00:00', '2025-07-25 09:20:00', 'CONCLUIDO', 'Paciente chegou adiantado.', 6), -- Criado por Recep. João
(4, 4, 4, '2025-07-28 15:00:00', '2025-07-28 15:40:00', 'CANCELADO_PACIENTE', 'Paciente cancelou por telefone com 24h de antecedência.', 7), -- Criado por Recep. Maria
(5, 5, 5, '2025-07-29 16:00:00', '2025-07-29 16:50:00', 'CONCLUIDO', NULL, 6); -- Criado por Recep. João

INSERT INTO tb_prontuarios (agendamento_id, paciente_id, medico_id, dados_triagem, sintomas, diagnostico, prescricao, solicitacao_exames) VALUES
(3, 3, 3, '{"pressao": "13/9", "temperatura": "36.8"}', 'Dor persistente no joelho direito após corrida.', 'Suspeita de tendinite patelar.', 'Anti-inflamatório por 7 dias e repouso.', 'Solicito ressonância magnética do joelho direito.'),
(5, 5, 5, '{"peso": "12.5kg", "altura": "85cm"}', 'Tosse seca há 3 dias, sem febre.', 'Infecção viral de vias aéreas superiores.', 'Hidratação, repouso e xarope para tosse se necessário.', 'Nenhum exame necessário no momento.');
