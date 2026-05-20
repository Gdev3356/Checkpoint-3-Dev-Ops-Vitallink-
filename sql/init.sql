-- ─────────────────────────────────────────────────────────────────────────────
-- VitalLink — dados iniciais para demonstração
-- Executado automaticamente pelo gvenzl/oracle-xe ao iniciar o container
-- (arquivos em /container-entrypoint-initdb.d são rodados com APP_USER)
-- ─────────────────────────────────────────────────────────────────────────────

-- Especialidades
INSERT INTO TBL_ESPECIALIDADE (NOME, DESCRICAO)
VALUES ('Clínica Geral', 'Atendimento geral e triagem inicial de pacientes');

INSERT INTO TBL_ESPECIALIDADE (NOME, DESCRICAO)
VALUES ('Cardiologia', 'Diagnóstico e tratamento de doenças do coração e sistema circulatório');

INSERT INTO TBL_ESPECIALIDADE (NOME, DESCRICAO)
VALUES ('Ortopedia', 'Tratamento de lesões e doenças do sistema músculo-esquelético');

INSERT INTO TBL_ESPECIALIDADE (NOME, DESCRICAO)
VALUES ('Pediatria', 'Cuidados médicos para crianças e adolescentes');

-- Médicos (especialidade 1 = Clínica Geral, 2 = Cardiologia)
INSERT INTO TBL_MEDICO (NOME, CRM, EMAIL, ID_ESPECIALIDADE, ATIVO)
VALUES ('Dr. Lucas Ferreira', 'CRM-SP-123456', 'lucas.ferreira@vitallink.com', 1, 'S');

INSERT INTO TBL_MEDICO (NOME, CRM, EMAIL, ID_ESPECIALIDADE, ATIVO)
VALUES ('Dra. Ana Paula Souza', 'CRM-SP-654321', 'ana.souza@vitallink.com', 2, 'S');

INSERT INTO TBL_MEDICO (NOME, CRM, EMAIL, ID_ESPECIALIDADE, ATIVO)
VALUES ('Dr. Roberto Lima', 'CRM-RJ-111222', 'roberto.lima@vitallink.com', 3, 'S');

-- Pacientes
INSERT INTO TBL_PACIENTE (NOME, CPF, EMAIL, TELEFONE, DATA_NASCIMENTO, DATA_CADASTRO)
VALUES ('Carlos Eduardo Silva', '123.456.789-00', 'carlos.silva@email.com',
        '(11) 98765-4321', DATE '1985-03-15', CURRENT_TIMESTAMP);

INSERT INTO TBL_PACIENTE (NOME, CPF, EMAIL, TELEFONE, DATA_NASCIMENTO, DATA_CADASTRO)
VALUES ('Maria Fernanda Costa', '987.654.321-00', 'maria.costa@email.com',
        '(11) 91234-5678', DATE '1992-07-22', CURRENT_TIMESTAMP);

INSERT INTO TBL_PACIENTE (NOME, CPF, EMAIL, TELEFONE, DATA_NASCIMENTO, DATA_CADASTRO)
VALUES ('João Pedro Almeida', '456.789.123-00', 'joao.almeida@email.com',
        '(21) 99876-5432', DATE '1978-11-30', CURRENT_TIMESTAMP);

-- Consultas
INSERT INTO TBL_CONSULTA (ID_PACIENTE, ID_MEDICO, DATA_HORA, STATUS_CONSULTA, OBSERVACOES)
VALUES (1, 1, TIMESTAMP '2025-06-10 09:00:00', 'AGENDADA', 'Consulta de rotina anual');

INSERT INTO TBL_CONSULTA (ID_PACIENTE, ID_MEDICO, DATA_HORA, STATUS_CONSULTA, OBSERVACOES)
VALUES (2, 2, TIMESTAMP '2025-06-12 14:30:00', 'AGENDADA', 'Avaliação eletrocardiograma');

INSERT INTO TBL_CONSULTA (ID_PACIENTE, ID_MEDICO, DATA_HORA, STATUS_CONSULTA, OBSERVACOES)
VALUES (3, 1, TIMESTAMP '2025-05-20 10:00:00', 'REALIZADA', 'Paciente apresentou melhora. Retorno em 30 dias.');

COMMIT;