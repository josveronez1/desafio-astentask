# AstenTask

API REST para gerenciamento de projetos e tarefas (desafio técnico).

## Clonar o projeto

```bash
git clone https://github.com/josveronez1/desafio-astentask.git
```

## Stack

- **Java 25** · **Spring Boot 4**
- **PostgreSQL** (banco de dados)
- **Spring Security** + JWT (Auth0)
- **Spring Data JPA** · **Bean Validation**
- **SpringDoc OpenAPI** (Swagger UI)

## Pré-requisitos

- Docker + Docker Compose
- JDK 25
- Maven 3.9+

## Infraestrutura com Docker

O projeto inclui um `docker-compose.yml` que sobe:

- **PostgreSQL 15** (banco `astentask_db`)
- **pgAdmin 4** (interface web para o banco)

Na raiz do projeto, execute:

```bash
docker compose up -d
```

Serviços:

- Postgres: `localhost:5432`
- pgAdmin: `http://localhost:5050`  
  - Email: `admin@admin.com`  
  - Senha: `admin`

A aplicação já está configurada para usar esse banco (ver `application.properties`).

> **Observação:** se você já tiver um PostgreSQL local usando a porta `5432`, pare esse serviço ou altere a porta local. O compose espera ficar com a `5432` livre para o container `db`.

## Como rodar a aplicação

1. Certifique-se de que o Docker Compose está rodando:

   ```bash
   docker compose ps
   ```

2. (Opcional) Configure a variável de ambiente do JWT:

   ```bash
   export JWT_SECRET=sua-chave-secreta
   ```

   Se não definir, é usado o valor padrão do `application.properties`.

3. Execute a aplicação:

   Linux / Mac:
   ```bash
   ./mvnw spring-boot:run
   ```

   Windows (PowerShell / CMD):
   ```bash
   mvnw.cmd spring-boot:run
   ```
   
4. Acesse o pgAdmin e cadastre um novo server:
    Host name / address: db
    Port: 5432
    Username: postgres
    Password: 1234


A API fica em **http://localhost:8080**.

## Documentação da API

Com a aplicação no ar, acesse o Swagger UI:

- **http://localhost:8080/swagger-ui.html**
- (ou **http://localhost:8080/swagger-ui/index.html**)

Lá estão todos os endpoints, modelos e a opção de testar as requisições.

## Endpoints principais

| Área          | Base path                      | Observação                              |
|---------------|--------------------------------|-----------------------------------------|
| Autenticação  | `/api/auth`                    | register, login, refresh, logout         |
| Usuários      | `/api/users`                  | CRUD + paginação                         |
| Projetos      | `/api/projects`               | CRUD + stats (`/api/projects/{id}/stats`) |
| Tarefas       | `/api/tasks`                  | CRUD + busca filtrada + assign           |
| Comentários   | `/api/tasks/{id}/comments`    | CRUD + paginação                         |
| Time logs     | `/api/tasks/{id}/timelogs`    | CRUD + paginação                         |
| Dashboard     | `/api/dashboard`              | resumo (projetos, tarefas, etc.)         |
| Relatórios    | `/api/reports/project/{id}`   | relatório do projeto                     |

Rotas protegidas exigem o header: `Authorization: Bearer <token>` (obtido em `/api/auth/login`).


## Testes
Linux / Mac
```bash
./mvnw test
```
Windows
```bash
mvnw.cmd test
```
Inclui testes unitários (services) e de integração (auth com H2 em memória).


## Decisões técnicas tomadas
- Projeto iniciado com Spring Initalizr, utilizando: Java 25, SpringBoot 4.0.3, Maven, Spring Security, PostgreSQL Driver, 
SpringData JPA, Spring Web. (Buscando fugir o mínimo da stack indicada no desafio)

- Comecei pela estruturação do projeto, buscando princípios de clean architechture, para conseguir manter a organização, facilitar manutenção e escalabilidade.

- Comecei pelo que já tinha domínio: Criação das entities, repositories, services e controllers. 
Logo após isso, já percebi a necessidade de implementar os DTOs.

- Depois que já tinha os CRUDs de todas as entidades criados até o controller, decidi implementar o JWT auth, 
outro conceito com o qual eu não tinha muita experiência e tive que aprender para conseguir utilizar.
(Se eu fosse fazer esse projeto novamente, eu já teria começado implementando o JWT Auth desde o começo, o que evitaria refatoração.)

- Após isso, também já configurei o banco de dados PostgreSQL, utilizando o docker para facilitar.

- Depois implementei a API externa, Brasil API, de uma maneira simples, onde ela verifica as datas de tasks dentro do projeto para ver se não são feriados ou afins.

- Depois adicionei o swagger para conseguir testar os endpoints mais visualmente

- A partir desse ponto, percebi que havia deixado muitos requisitos obrigatórios pra trás, que, novamente, se refizesse o projeto, já os implementaria no desenvolvimento inicial.

- Adicionei autorização por roles, logout e refresh, paginação e ordenação, filtros avançados, 
globalexceptionhandler para centralizar exceptions, bean validation para validação mais robusta, estatisticas e relatorios de projetos, 
testes unitários, e por fim os logs.

- Essa foi a parte mais difícil do projeto, todas essas adições demandaram refatoração, criação de novas classes e dtos, o que poderia ter sido evitado com um planejamento de implementação um pouco melhor.

## Visão geral do projeto
- Foram entregues: Requisitos obrigatórios do desafio e alguns dos diferenciais. 

- O projeto serviu como um grande project learning, visto que para seu desenvolvimento, 
precisei buscar e adquirir muito conhecimento que ainda não tinha domínio. 
Acredito que a partir dele consegui aprofundar meus conhecimentos na linguagem Java, na qual me considero iniciante.

- Utilização de forma consciente de IA, utilizei o gemini (ferramenta mais simples) como um "professor / parceiro dev", que forneceu ajuda para entender melhor conceitos e 
como eles se conectam e code snippets para pegar sintaxe. Nenhum "agente autônomo" utilizado para escrever código.
Busquei utilizar a ferramenta somente como facilitadora para uma aprendizagem e desenvolvimento mais rápidos.