# VitalLink API

Sistema de agendamento de consultas médicas — conecta pacientes e médicos,
gerenciando especialidades, agendamentos e histórico clínico.

**Stack:** Java 17 · Spring Boot 3 · JPA/Hibernate · Oracle XE 21c · Docker

---

## 1. Dependências do Spring Initializr

Acesse [start.spring.io](https://start.spring.io) com as configurações abaixo:

| Campo       | Valor               |
|-------------|---------------------|
| Project     | Maven               |
| Language    | Java                |
| Spring Boot | 3.3.x (ou superior) |
| Group       | `br.com`            |
| Artifact    | `vitallink`         |
| Packaging   | Jar                 |
| Java        | 17                  |

### Dependências para selecionar no Initializr

| Dependência            | Descrição                              |
|------------------------|----------------------------------------|
| **Spring Web**         | Controllers REST                       |
| **Spring Data JPA**    | Repositórios e mapeamento ORM          |
| **Oracle Driver**      | Driver JDBC para Oracle                |
| **Lombok**             | Redução de boilerplate                 |
| **Validation**         | Bean Validation (Jakarta)              |
| **Spring DevTools**    | Hot reload em desenvolvimento          |

### Dependências para adicionar manualmente no `pom.xml` (Caso esteja tentando recriar a aplicação JAVA do zero por algum motivo)

```xml
<!-- Swagger / OpenAPI UI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>

<!-- Cache (já incluso no Spring Boot, mas declarar explicitamente) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

---

## 2. Estrutura de pacotes

```
br.com.fiap.vitallink
├── VitallinkApplication.java
├── config/
│   └── SwaggerConfig.java
├── controllers/
│   ├── ConsultaController.java
│   ├── EspecialidadeController.java
│   ├── MedicoController.java
│   └── PacienteController.java
├── dto/
│   ├── ConsultaRequestDTO.java / ConsultaResponseDTO.java
│   ├── EspecialidadeRequestDTO.java / EspecialidadeResponseDTO.java
│   ├── MedicoRequestDTO.java / MedicoResponseDTO.java
│   └── PacienteRequestDTO.java / PacienteResponseDTO.java
├── entities/
│   ├── Consulta.java
│   ├── Especialidade.java
│   ├── Medico.java
│   └── Paciente.java
├── enums/
│   └── StatusConsulta.java
├── exceptions/
│   ├── ApiError.java
│   ├── BusinessException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repositories/
│   ├── ConsultaRepository.java
│   ├── EspecialidadeRepository.java
│   ├── MedicoRepository.java
│   └── PacienteRepository.java
└── services/
    ├── ConsultaService.java
    ├── EspecialidadeService.java
    ├── MedicoService.java
    └── PacienteService.java
```

---

## 3. Modelo de dados (DER simplificado)

```
TBL_ESPECIALIDADE
  ID_ESPECIALIDADE (PK)
  NOME
  DESCRICAO

TBL_MEDICO
  ID_MEDICO (PK)
  NOME, CRM, EMAIL, ATIVO
  ID_ESPECIALIDADE (FK → TBL_ESPECIALIDADE)

TBL_PACIENTE
  ID_PACIENTE (PK)
  NOME, CPF, EMAIL, TELEFONE
  DATA_NASCIMENTO, DATA_CADASTRO

TBL_CONSULTA
  ID_CONSULTA (PK)
  ID_PACIENTE (FK → TBL_PACIENTE)
  ID_MEDICO   (FK → TBL_MEDICO)
  DATA_HORA, STATUS_CONSULTA, OBSERVACOES
```

---

## 4. Endpoints disponíveis

| Recurso         | Método | Rota                                          |
|-----------------|--------|-----------------------------------------------|
| Especialidades  | GET    | `/api/especialidades?page=0&size=10&sort=nome` |
| Especialidades  | GET    | `/api/especialidades/{id}`                    |
| Especialidades  | POST   | `/api/especialidades`                         |
| Especialidades  | PUT    | `/api/especialidades/{id}`                    |
| Especialidades  | DELETE | `/api/especialidades/{id}`                    |
| Médicos         | GET    | `/api/medicos?nome=ana&page=0`                |
| Médicos         | GET    | `/api/medicos/especialidade/{id}`             |
| Médicos         | POST   | `/api/medicos`                                |
| Médicos         | PUT    | `/api/medicos/{id}`                           |
| Médicos         | DELETE | `/api/medicos/{id}`  *(soft delete)*          |
| Pacientes       | GET    | `/api/pacientes?nome=carlos`                  |
| Pacientes       | POST   | `/api/pacientes`                              |
| Pacientes       | PUT    | `/api/pacientes/{id}`                         |
| Pacientes       | DELETE | `/api/pacientes/{id}`                         |
| Consultas       | GET    | `/api/consultas?status=AGENDADA`              |
| Consultas       | GET    | `/api/consultas/periodo?inicio=...&fim=...`   |
| Consultas       | POST   | `/api/consultas`                              |
| Consultas       | PUT    | `/api/consultas/{id}`                         |
| Consultas       | DELETE | `/api/consultas/{id}` *(cancela consulta)*    |
| Swagger UI      | GET    | `/swagger-ui.html`                            |

---

## 5. Executar localmente (8 passos do Challenge)

### Passo 1 — Compilar
```bash
mvn clean package -DskipTests
```

### Passo 2 — Criar Dockerfile e docker-compose.yml
Já criados. Verifique com:
```bash
docker compose config
```

### Passo 3 — Build e versionar imagens
```bash
docker build -t vitallink:v1 .
docker build -t vitallink:v2 .
docker image ls vitallink

# Confirmar que NÃO roda como root
docker run --rm --entrypoint whoami vitallink:v1
# deve retornar: appuser
```

### Passo 4 — Executar local
```bash
# Testar a imagem isolada (sem banco)
docker run --name vitallink-test -p 8080:8080 -d vitallink:v1
docker ps && docker stop vitallink-test && docker rm vitallink-test

# Subir App + Oracle juntos
docker compose up --build -d
docker compose ps
docker volume ls
```

### Passo 5 — Docker push
```bash
export DOCKERHUB_USER="seu_usuario"
docker tag vitallink:v1 "$DOCKERHUB_USER/vitallink:v1"
docker tag vitallink:v2 "$DOCKERHUB_USER/vitallink:v2"
docker tag vitallink:v1 "$DOCKERHUB_USER/vitallink:latest"
docker login
docker push "$DOCKERHUB_USER/vitallink:v1"
docker push "$DOCKERHUB_USER/vitallink:v2"
docker push "$DOCKERHUB_USER/vitallink:latest"
```

### Passo 6 — Azure CLI
```bash
chmod +x azure-infra.sh && ./azure-infra.sh
```

### Passo 7 — Deploy na VM
```bash
ssh azureuser@<IP_PUBLICO>
git clone https://github.com/Gdev3356/Checkpoint-3-Dev-Ops-Vitallink-
cd vitallink
docker pull SEU_USUARIO/vitallink:v1
docker pull gvenzl/oracle-xe:21-slim
docker compose up -d
docker compose ps
docker logs vitallink-app --tail 50
docker exec vitallink-app whoami     # deve retornar: appuser
```

### Passo 8 — CRUD externo (evidência para a banca)
```bash
IP="<IP_PUBLICO_DA_VM>"

# GET — listar especialidades
curl http://$IP:8080/api/especialidades

# POST — criar paciente
curl -X POST http://$IP:8080/api/pacientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Tutor Banca","cpf":"111.222.333-44","email":"banca@fiap.com.br","telefone":"(11) 99999-0000","dataNascimento":"1990-01-01"}'

# PUT — atualizar
curl -X PUT http://$IP:8080/api/pacientes/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Tutor Atualizado","cpf":"111.222.333-44","email":"banca.novo@fiap.com.br","telefone":"(11) 88888-0000","dataNascimento":"1990-01-01"}'

# DELETE — cancelar consulta
curl -X DELETE http://$IP:8080/api/consultas/1

# Verificar dados no Oracle
docker exec -it oracle-db sqlplus FiapAdmin123/FiapSecret543@XEPDB1
SELECT * FROM TBL_PACIENTE;
SELECT * FROM TBL_CONSULTA;
SELECT table_name FROM user_tables;
```

---

## 6. Remover recursos Azure após a avaliação

```bash
az group delete --name rg-vitallink-challenge --yes --no-wait
```