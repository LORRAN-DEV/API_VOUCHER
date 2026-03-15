# API Voucher

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?logo=docker)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-green?logo=swagger)
![Status](https://img.shields.io/badge/Status-Completo-green)

---

## Introducao

**API Voucher** e um sistema completo de **gerenciamento de vouchers de desconto**, desenvolvido em **Java com Spring Boot 3.5**, **MySQL** e **Docker**.

O sistema permite criar, aplicar, desativar e rastrear o uso de vouchers de desconto, com controle de validade, limite de usos e historico de utilizacao.

---

## Como Executar com Docker

### Pre-requisitos
- Docker
- Docker Compose

### Execucao
```bash
git clone https://github.com/LORRAN-DEV/API_VOUCHER.git
cd API_VOUCHER/api-voucher/api-voucher
docker-compose up --build
```

### Acessos
| Servico         | URL                                      |
|-----------------|------------------------------------------|
| API REST        | http://localhost:8080                     |
| Swagger UI      | http://localhost:8080/swagger-ui.html     |
| Banco MySQL     | localhost:3306                            |

---

## Arquitetura do Sistema

### Componentes
| Camada       | Tecnologia                        |
|--------------|-----------------------------------|
| Backend      | Spring Boot 3.5 + JPA/Hibernate  |
| Banco        | MySQL 8.0                         |
| Seguranca    | Spring Security                   |
| Documentacao | SpringDoc OpenAPI (Swagger)       |
| Container    | Docker + Docker Compose           |
| Build        | Maven                             |

### Padrao Arquitetural
Arquitetura em **3 camadas**:
1. **Controller** -- Recebe requisicoes HTTP (REST)
2. **Service** -- Logica de negocio e validacoes
3. **Repository** -- Acesso ao banco via Spring Data JPA

---

## Tecnologias e Dependencias

| Tecnologia                  | Versao   | Funcao                            |
|-----------------------------|----------|-----------------------------------|
| Java                        | 17       | Linguagem principal               |
| Spring Boot                 | 3.5.3    | Framework web                     |
| Spring Data JPA             | -        | ORM e acesso a dados              |
| Spring Security             | -        | Autenticacao e autorizacao        |
| SpringDoc OpenAPI           | 2.1.0    | Documentacao Swagger automatica   |
| MySQL Connector             | -        | Driver de banco de dados          |
| Lombok                      | -        | Reducao de boilerplate            |
| Docker                      | -        | Containerizacao                   |
| Maven                       | -        | Gerenciamento de dependencias     |

---

## Endpoints da API

| Metodo   | Endpoint                          | Descricao                          |
|----------|-----------------------------------|------------------------------------|
| POST     | /api/vouchers                     | Cria um novo voucher               |
| GET      | /api/vouchers                     | Lista todos os vouchers            |
| GET      | /api/vouchers/{code}              | Busca voucher por codigo           |
| POST     | /api/vouchers/{code}/apply        | Aplica/utiliza um voucher          |
| PUT      | /api/vouchers/{id}/deactivate     | Desativa um voucher                |
| DELETE   | /api/vouchers/{id}                | Remove um voucher                  |
| GET      | /api/vouchers/{id}/usage          | Historico de uso do voucher        |

### Regras de Aplicacao de Voucher
Ao aplicar um voucher (POST /api/vouchers/{code}/apply), o sistema valida:
1. Se o voucher existe
2. Se esta ativo (active = true)
3. Se nao expirou (expirationDate maior que agora)
4. Se nao atingiu o limite de usos (currentUses menor que maxUses)

---

## Modelo de Dados

### Voucher
| Campo                | Tipo           | Descricao                            |
|----------------------|----------------|--------------------------------------|
| id                   | Long (PK, AI)  | Identificador unico                  |
| code                 | String (UNIQUE)| Codigo do voucher                    |
| description          | String         | Descricao do desconto                |
| discountPercentage   | Double         | Percentual de desconto               |
| maxUses              | Integer        | Limite maximo de utilizacoes         |
| currentUses          | Integer        | Usos realizados (default: 0)        |
| active               | Boolean        | Status ativo/inativo (default: true) |
| expirationDate       | LocalDateTime  | Data de expiracao                    |
| createdAt            | LocalDateTime  | Data de criacao                      |

### VoucherUsage
| Campo      | Tipo           | Descricao                        |
|------------|----------------|----------------------------------|
| id         | Long (PK, AI)  | Identificador unico              |
| voucher    | Voucher (FK)   | Relacionamento ManyToOne         |
| usedBy     | String         | Identificador de quem usou       |
| usedAt     | LocalDateTime  | Data/hora de utilizacao          |

---

## Testando a API

### 1. Criar Voucher
```bash
curl -X POST http://localhost:8080/api/vouchers \
  -H "Content-Type: application/json" \
  -d '{"code":"DESCONTO20","discountPercentage":20.0,"maxUses":100,"expirationDate":"2026-12-31T23:59:59"}'
```

### 2. Listar Vouchers
```bash
curl http://localhost:8080/api/vouchers
```

### 3. Buscar por Codigo
```bash
curl http://localhost:8080/api/vouchers/DESCONTO20
```

### 4. Aplicar Voucher
```bash
curl -X POST http://localhost:8080/api/vouchers/DESCONTO20/apply \
  -H "Content-Type: application/json" \
  -d '"usuario@email.com"'
```

### 5. Ver Historico de Uso
```bash
curl http://localhost:8080/api/vouchers/1/usage
```

### 6. Desativar Voucher
```bash
curl -X PUT http://localhost:8080/api/vouchers/1/deactivate
```

### 7. Remover Voucher
```bash
curl -X DELETE http://localhost:8080/api/vouchers/1
```

---

## Configuracao do Banco

| Propriedade  | Valor             |
|--------------|-------------------|
| Database     | api_voucher_db    |
| Username     | root              |
| Password     | 123456            |
| Port         | 3306              |
| DDL Auto     | update            |

---

## Desenvolvimento Local

### Sem Docker
```bash
cd api-voucher/api-voucher
mvn clean package
mvn spring-boot:run
```

### Com Docker
```bash
cd api-voucher/api-voucher
docker-compose up --build
```

### Variaveis de Ambiente
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/api_voucher_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=123456
```

---

## Passos de Implementacao

Este projeto foi construido seguindo os passos:

1. **Inicializacao** -- Scaffold Spring Boot com Spring Initializr (Web, JPA, Security, MySQL, Lombok, OpenAPI)
2. **Modelagem** -- Criacao das entidades JPA Voucher e VoucherUsage com relacionamento ManyToOne
3. **Repositorios** -- Interfaces Spring Data JPA com queries customizadas (findByCode, findByActiveTrue)
4. **Servico** -- Logica de negocio com validacao de voucher (ativo, expirado, limite de usos) e controle transacional
5. **Controller** -- Endpoints REST com CORS habilitado e respostas HTTP adequadas (200, 201, 400, 404)
6. **Seguranca** -- Configuracao Spring Security permissiva para desenvolvimento (CSRF desabilitado, todas as rotas abertas)
7. **Containerizacao** -- Dockerfile (OpenJDK 17) e docker-compose.yml com MySQL 8.0 e volumes persistentes
8. **Documentacao** -- Swagger UI automatico via SpringDoc OpenAPI

---

## Resumo

- API REST completa para gerenciamento de vouchers de desconto
- CRUD com validacoes de negocio (validade, limite de usos, status)
- Rastreamento de historico de utilizacao por voucher
- Containerizacao com Docker e Docker Compose
- Documentacao automatica com Swagger/OpenAPI
- Spring Security configurado
- Banco MySQL com schema auto-gerenciado pelo Hibernate
