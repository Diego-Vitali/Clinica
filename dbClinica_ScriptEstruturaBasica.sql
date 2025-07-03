drop database dbClinica

create database dbClinica;

USE dbClinica;

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
-- Esta tabela é crucial para a lógica do agendamento.
CREATE TABLE tb_procedimentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    especialidade_id INT NOT NULL, -- Qual especialidade realiza este procedimento.
    tipo_sala_id INT NOT NULL, -- Qual tipo de sala é necessária.
    nome VARCHAR(150) NOT NULL,
    duracao_minutos INT NOT NULL, -- Duração padrão do procedimento.
    valor DECIMAL(10, 2),
    instrucoes_preparo TEXT, -- Instruções para o paciente.
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (especialidade_id) REFERENCES tb_especialidades(id),
    FOREIGN KEY (tipo_sala_id) REFERENCES tb_tipos_sala(id)
);

-- TABELAS DE USUÁRIOS E PESSOAS

-- Tabela central de usuários para controle de acesso ao sistema.
CREATE TABLE tb_usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('PACIENTE', 'MEDICO', 'RECEPCIONISTA', 'ADMIN') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabela com os dados cadastrais dos pacientes.
CREATE TABLE tb_pacientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT UNIQUE, -- Pode ser nulo se o paciente for cadastrado por um funcionário e ainda não tiver acesso.
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
    usuario_id INT UNIQUE NOT NULL, -- Todo funcionário precisa de um login.
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    cargo VARCHAR(100) NOT NULL, -- Ex: 'Recepcionista', 'Enfermeiro(a)', 'Administrador(a)'
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

-- TABELAS OPERACIONAIS (AGENDAMENTO E PRONTUÁRIOS)

-- Tabela para definir os horários de trabalho de cada médico.
CREATE TABLE tb_horarios_trabalho (
    id INT PRIMARY KEY AUTO_INCREMENT,
    medico_id INT NOT NULL,
    dia_semana ENUM('DOMINGO', 'SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    UNIQUE(medico_id, dia_semana, hora_inicio, hora_fim), -- Evita duplicidade
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
    observacoes TEXT, -- Anotações da recepção sobre o agendamento.
    criado_por_usuario_id INT NOT NULL, -- Quem fez o agendamento (um paciente ou funcionário).
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES tb_pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES tb_medicos(id),
    FOREIGN KEY (procedimento_id) REFERENCES tb_procedimentos(id),
    FOREIGN KEY (sala_id) REFERENCES tb_salas(id),
    FOREIGN KEY (criado_por_usuario_id) REFERENCES tb_usuarios(id)
);

-- Tabela para o prontuário eletrônico do paciente, gerado após um atendimento.
CREATE TABLE tb_prontuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    agendamento_id INT UNIQUE NOT NULL, -- Cada prontuário está ligado a um único agendamento concluído.
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    dados_triagem TEXT, -- Ex: JSON com {pressao: '12/8', temperatura: '36.5', peso: '70.5'}
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