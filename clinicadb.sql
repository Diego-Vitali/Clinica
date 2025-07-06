-- Apaga o banco de dados se ele já existir, para um começo limpo.
DROP DATABASE IF EXISTS dbClinica;

-- Cria o banco de dados.
CREATE DATABASE dbClinica;

-- Seleciona o banco de dados para uso.
USE dbClinica;

-- ESTRUTURA DAS TABELAS

-- Tabela para gerenciar todas as especialidades médicas oferecidas.
CREATE TABLE tb_especialidades (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT
);

-- Tabela para cadastrar os tipos de sala (Ex: Consultório, Sala de Exame de Imagem).
CREATE TABLE tb_tipos_sala (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT
);

-- Tabela para gerenciar todas as salas físicas da clínica.
CREATE TABLE tb_salas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo_sala_id INT NOT NULL,
    nome VARCHAR(50) NOT NULL, -- Ex: "Sala 101", "Raio-X"
    localizacao VARCHAR(100), -- Ex: "2º Andar, Ala B"
    ativa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (tipo_sala_id) REFERENCES tb_tipos_sala(id)
);

-- Tabela para gerenciar os procedimentos (consultas, exames) que a clínica oferece.
CREATE TABLE tb_procedimentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    especialidade_id INT NOT NULL,
    tipo_sala_id INT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    duracao_minutos INT NOT NULL,
    valor DECIMAL(10, 2),
    instrucoes_preparo TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (especialidade_id) REFERENCES tb_especialidades(id),
    FOREIGN KEY (tipo_sala_id) REFERENCES tb_tipos_sala(id)
);

-- Tabela central de usuários para controle de acesso ao sistema.
CREATE TABLE tb_usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL, -- Em um sistema real, armazene senhas com hash.
    tipo_usuario ENUM('PACIENTE', 'MEDICO', 'RECEPCIONISTA', 'ADMIN') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabela com os dados cadastrais dos pacientes.
CREATE TABLE tb_pacientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT UNIQUE,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    data_nascimento DATE,
    telefone VARCHAR(20),
    endereco TEXT,
    convenio VARCHAR(100),
    numero_carteirinha VARCHAR(100),
    FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id)
);

-- Tabela com os dados de todos os funcionários (incluindo médicos).
CREATE TABLE tb_funcionarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT UNIQUE NOT NULL,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    data_admissao DATE,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id)
);

-- Tabela específica para dados dos médicos, ligada a um funcionário.
CREATE TABLE tb_medicos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    funcionario_id INT UNIQUE NOT NULL,
    crm VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (funcionario_id) REFERENCES tb_funcionarios(id)
);

-- Tabela de associação (N:N) entre médicos e suas especialidades.
CREATE TABLE tb_medico_especialidades (
    medico_id INT NOT NULL,
    especialidade_id INT NOT NULL,
    PRIMARY KEY (medico_id, especialidade_id),
    FOREIGN KEY (medico_id) REFERENCES tb_medicos(id),
    FOREIGN KEY (especialidade_id) REFERENCES tb_especialidades(id)
);

-- Tabela para definir os horários de trabalho de cada médico.
CREATE TABLE tb_horarios_trabalho (
    id INT PRIMARY KEY AUTO_INCREMENT,
    medico_id INT NOT NULL,
    dia_semana ENUM('DOMINGO', 'SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    UNIQUE(medico_id, dia_semana, hora_inicio, hora_fim),
    FOREIGN KEY (medico_id) REFERENCES tb_medicos(id)
);

-- Tabela central de agendamentos.
CREATE TABLE tb_agendamentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    procedimento_id INT NOT NULL,
    sala_id INT NOT NULL,
    data_hora_inicio DATETIME NOT NULL,
    data_hora_fim DATETIME NOT NULL,
    status ENUM('AGENDADO', 'CONCLUIDO', 'CANCELADO_PACIENTE', 'CANCELADO_CLINICA', 'NAO_COMPARECEU') NOT NULL DEFAULT 'AGENDADO',
    observacoes TEXT,
    criado_por_usuario_id INT NOT NULL,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES tb_pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES tb_medicos(id),
    FOREIGN KEY (procedimento_id) REFERENCES tb_procedimentos(id),
    FOREIGN KEY (sala_id) REFERENCES tb_salas(id),
    FOREIGN KEY (criado_por_usuario_id) REFERENCES tb_usuarios(id)
);

-- Tabela para o prontuário eletrônico do paciente.
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


-- INSERÇÃO DE DADOS PARA TESTE

-- Inserindo 5 especialidades
INSERT INTO tb_especialidades (nome, descricao) VALUES
('Cardiologia', 'Cuida de doenças do coração e do sistema circulatório.'),
('Dermatologia', 'Trata de doenças da pele, cabelos e unhas.'),
('Ortopedia', 'Especialidade que cuida de lesões e doenças do sistema locomotor.'),
('Pediatria', 'Cuida da saúde de crianças e adolescentes.'),
('Ginecologia', 'Especialidade voltada para a saúde do sistema reprodutor feminino.');

-- Inserindo 5 tipos de sala
INSERT INTO tb_tipos_sala (nome, descricao) VALUES
('Consultório Padrão', 'Sala para consultas gerais e de especialistas.'),
('Sala de Exames de Imagem', 'Equipada para Raio-X, Ultrassom, etc.'),
('Sala de Procedimentos', 'Para pequenas cirurgias e procedimentos ambulatoriais.'),
('Consultório Pediátrico', 'Consultório adaptado para o atendimento de crianças.'),
('Laboratório de Coleta', 'Sala para coleta de amostras de sangue e outros materiais biológicos.');

-- Inserindo 5 salas
INSERT INTO tb_salas (tipo_sala_id, nome, localizacao, ativa) VALUES
(1, 'Consultório 101', '1º Andar, Ala A', TRUE),
(1, 'Consultório 102', '1º Andar, Ala A', TRUE),
(2, 'Sala de Raio-X', 'Térreo, Ala C', TRUE),
(3, 'Sala de Pequenos Procedimentos', '2º Andar, Bloco B', TRUE),
(5, 'Coleta 1', 'Térreo, Ala B', TRUE);

-- Inserindo 15 usuários (5 de cada tipo relevante + extras)
INSERT INTO tb_usuarios (email, senha, tipo_usuario, ativo) VALUES
-- Pacientes
('carlos.santos@email.com', 'senha123', 'PACIENTE', TRUE),
('ana.pereira@email.com', 'senha123', 'PACIENTE', TRUE),
('bruno.costa@email.com', 'senha123', 'PACIENTE', TRUE),
('fernanda.lima@email.com', 'senha123', 'PACIENTE', TRUE),
('juliana.alves@email.com', 'senha123', 'PACIENTE', TRUE),
-- Médicos
('dr.rodrigo.mendes@email.com', 'senhaMed1', 'MEDICO', TRUE),
('dra.beatriz.gomes@email.com', 'senhaMed2', 'MEDICO', TRUE),
('dr.lucas.ferreira@email.com', 'senhaMed3', 'MEDICO', TRUE),
('dra.carla.souza@email.com', 'senhaMed4', 'MEDICO', TRUE),
('dr.marcelo.oliveira@email.com', 'senhaMed5', 'MEDICO', TRUE),
-- Recepcionistas
('recep.joao@email.com', 'senhaRec1', 'RECEPCIONISTA', TRUE),
('recep.maria@email.com', 'senhaRec2', 'RECEPCIONISTA', TRUE),
-- Admin
('admin.geral@email.com', 'senhaAdm1', 'ADMIN', TRUE),
('ti.suporte@email.com', 'senhaAdm2', 'ADMIN', TRUE),
('financeiro.chefe@email.com', 'senhaAdm3', 'ADMIN', TRUE);


-- Inserindo 5 pacientes
INSERT INTO tb_pacientes (usuario_id, nome_completo, cpf, data_nascimento, telefone, endereco, convenio, numero_carteirinha) VALUES
(1, 'Carlos Santos', '111.222.333-44', '1985-05-20', '(11) 98877-6655', 'Rua das Flores, 123, São Paulo, SP', 'Amil', '123456789'),
(2, 'Ana Pereira', '222.333.444-55', '1992-11-30', '(21) 99988-7766', 'Avenida Copacabana, 456, Rio de Janeiro, RJ', 'Bradesco Saúde', '987654321'),
(3, 'Bruno Costa', '333.444.555-66', '1978-01-15', '(31) 98765-4321', 'Rua da Bahia, 789, Belo Horizonte, MG', 'Unimed', 'A1B2C3D4E5'),
(4, 'Fernanda Lima', '444.555.666-77', '2001-07-10', '(51) 99123-4567', 'Avenida Ipiranga, 101, Porto Alegre, RS', 'SulAmérica', 'F6G7H8I9J0'),
(5, 'Juliana Alves', '555.666.777-88', '1995-03-25', '(71) 98888-9999', 'Rua do Carmo, 202, Salvador, BA', 'Amil', 'K1L2M3N4P5');

-- Inserindo 7 funcionários (5 médicos, 2 recepcionistas)
INSERT INTO tb_funcionarios (usuario_id, nome_completo, cpf, cargo, telefone, data_admissao, ativo) VALUES
(6, 'Rodrigo Mendes', '123.456.789-01', 'Médico', '(11) 91111-1111', '2020-01-10', TRUE),
(7, 'Beatriz Gomes', '234.567.890-12', 'Médico', '(11) 92222-2222', '2018-05-22', TRUE),
(8, 'Lucas Ferreira', '345.678.901-23', 'Médico', '(11) 93333-3333', '2021-03-15', TRUE),
(9, 'Carla Souza', '456.789.012-34', 'Médico', '(11) 94444-4444', '2019-08-01', TRUE),
(10, 'Marcelo Oliveira', '567.890.123-45', 'Médico', '(11) 95555-5555', '2022-02-20', TRUE),
(11, 'João da Silva', '678.901.234-56', 'Recepcionista', '(11) 96666-6666', '2017-11-01', TRUE),
(12, 'Maria Oliveira', '789.012.345-67', 'Recepcionista', '(11) 97777-7777', '2023-01-30', TRUE);

-- Inserindo os 5 médicos na tabela de médicos
INSERT INTO tb_medicos (funcionario_id, crm) VALUES
(1, 'SP/123456'),
(2, 'SP/234567'),
(3, 'SP/345678'),
(4, 'SP/456789'),
(5, 'SP/567890');

-- Associando médicos às suas especialidades
INSERT INTO tb_medico_especialidades (medico_id, especialidade_id) VALUES
(1, 1), -- Dr. Rodrigo (Cardiologia)
(2, 2), -- Dra. Beatriz (Dermatologia)
(3, 3), -- Dr. Lucas (Ortopedia)
(4, 5), -- Dra. Carla (Ginecologia)
(4, 2), -- Dra. Carla (também Dermatologia)
(5, 4); -- Dr. Marcelo (Pediatria)

-- Inserindo 5 procedimentos
INSERT INTO tb_procedimentos (especialidade_id, tipo_sala_id, nome, duracao_minutos, valor, instrucoes_preparo, ativo) VALUES
(1, 1, 'Consulta Cardiológica', 45, 350.00, 'Trazer exames anteriores, se houver.', TRUE),
(2, 1, 'Consulta Dermatológica', 30, 300.00, 'Não usar maquiagem ou cremes na área a ser examinada.', TRUE),
(3, 1, 'Consulta Ortopédica - Retorno', 20, 200.00, 'Trazer exames de imagem recentes.', TRUE),
(5, 1, 'Consulta Ginecológica de Rotina', 40, 320.00, 'Abstinência sexual de 48h antes da consulta.', TRUE),
(4, 4, 'Consulta Pediátrica', 50, 280.00, 'Trazer a carteira de vacinação da criança.', TRUE);

-- Inserindo horários de trabalho para 3 médicos
INSERT INTO tb_horarios_trabalho (medico_id, dia_semana, hora_inicio, hora_fim) VALUES
(1, 'SEGUNDA', '08:00:00', '12:00:00'), -- Dr. Rodrigo
(1, 'QUARTA', '14:00:00', '18:00:00'), -- Dr. Rodrigo
(2, 'TERCA', '09:00:00', '17:00:00'),   -- Dra. Beatriz
(2, 'QUINTA', '09:00:00', '17:00:00'),  -- Dra. Beatriz
(3, 'SEXTA', '08:00:00', '15:00:00');  -- Dr. Lucas

-- Inserindo 5 agendamentos
INSERT INTO tb_agendamentos (paciente_id, medico_id, procedimento_id, sala_id, data_hora_inicio, data_hora_fim, status, observacoes, criado_por_usuario_id) VALUES
(1, 1, 1, 1, '2025-07-10 10:00:00', '2025-07-10 10:45:00', 'AGENDADO', 'Paciente ligou para confirmar.', 11),
(2, 2, 2, 2, '2025-07-11 11:00:00', '2025-07-11 11:30:00', 'AGENDADO', 'Primeira consulta.', 2),
(3, 3, 3, 1, '2025-07-12 09:00:00', '2025-07-12 09:20:00', 'CONCLUIDO', 'Paciente chegou adiantado.', 12),
(4, 4, 4, 1, '2025-07-14 15:00:00', '2025-07-14 15:40:00', 'CANCELADO_PACIENTE', 'Paciente cancelou por telefone com 24h de antecedência.', 4),
(5, 5, 5, 1, '2025-07-15 16:00:00', '2025-07-15 16:50:00', 'CONCLUIDO', NULL, 11);

-- Inserindo 2 prontuários para os agendamentos concluídos
INSERT INTO tb_prontuarios (agendamento_id, paciente_id, medico_id, dados_triagem, sintomas, diagnostico, prescricao, solicitacao_exames) VALUES
(3, 3, 3, '{"pressao": "13/9", "temperatura": "36.8"}', 'Dor persistente no joelho direito após corrida.', 'Suspeita de tendinite patelar.', 'Anti-inflamatório por 7 dias e repouso.', 'Solicito ressonância magnética do joelho direito.'),
(5, 5, 5, '{"peso": "12.5kg", "altura": "85cm"}', 'Tosse seca há 3 dias, sem febre.', 'Infecção viral de vias aéreas superiores.', 'Hidratação, repouso e xarope para tosse se necessário.', 'Nenhum exame necessário no momento.');

