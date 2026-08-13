# ERP Java

> ERP desenvolvido do zero utilizando Java e Spring Boot.

## Sobre o projeto

Opa, pessoal!

Este é um projeto de **ERP desenvolvido do zero em Java**, utilizando **Spring Boot** e diversas bibliotecas do ecossistema Java, sem usar **IA** para o desenvolvimento!.

A ideia principal do projeto é construir uma aplicação empresarial completa, passando por todas as etapas do desenvolvimento: modelagem do banco de dados, regras de negócio, APIs, autenticação, testes, Docker, filas e, futuramente, um frontend independente.

Atualmente, o frontend utiliza Thymeleaf, mas o objetivo futuro é desenvolver uma interface totalmente independente utilizando React ou outra tecnologia que atenda às necessidades do projeto.

O projeto também tem como objetivo servir como experiência prática de desenvolvimento de software, arquitetura, boas práticas e integração entre diferentes tecnologias.

Se você tiver alguma dúvida, quiser sugerir alguma melhoria ou quiser participar do desenvolvimento, fique à vontade para entrar em contato e contribuir com o projeto!

---

## Tecnologias utilizadas

Atualmente, o projeto utiliza principalmente:

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **Spring Security**
* **PostgreSQL**
* **Thymeleaf**
* **Maven**
* **Docker**
* **Docker Compose**
* **JUnit**
* **Mockito**

Outras tecnologias e bibliotecas podem ser adicionadas conforme o desenvolvimento do projeto.

---
## Documentação da API

A API do ERP possui uma collection no Postman com os endpoints disponíveis para consulta, testes e desenvolvimento.

A collection é atualizada conforme novos endpoints e funcionalidades são implementados.

👉 **[Acessar documentação da API no Postman](https://www.postman.com/gustavokrauss-team/workspace/erp-java/collection/26896146-6e81010b-94e7-4d7c-b19b-dc36a65a3f90)**

---

## Como executar

Para executar o projeto localmente, você precisa ter o **Docker** e o **Docker Compose** instalados.

### 1. Clone o repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd <PASTA_DO_PROJETO>
```

### 2. Crie o arquivo `.env`

Na raiz do projeto, crie um arquivo chamado `.env` com as seguintes informações:

```env
POSTGRES_DB=ERP
POSTGRES_USER=postgres
POSTGRES_PASSWORD=1234

POSTGRES_DB_TEST=ERP
POSTGRES_USER_TEST=postgres
POSTGRES_PASSWORD_TEST=1234

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ERP
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=1234
```

Essas configurações são destinadas ao ambiente de desenvolvimento.

As credenciais podem ser alteradas conforme necessário, porém algumas configurações estão integradas ao ambiente Docker e à aplicação. Alterações podem exigir ajustes adicionais no `docker-compose.yml` e nas configurações do Spring Boot.

> **Importante:** não versione o seu arquivo `.env` no Git. Utilize um `.env.example` para disponibilizar as configurações necessárias sem expor credenciais reais.

### 3. Execute o projeto

Com o Docker em execução, basta utilizar:

```bash
docker compose up -d
```

O Docker Compose já está configurado para subir os serviços necessários automaticamente.

Para acompanhar os logs:

```bash
docker compose logs -f
```

Para parar os containers:

```bash
docker compose down
```

---

## Banco de dados

O projeto utiliza **PostgreSQL** como banco de dados principal.

A configuração do banco já está integrada ao ambiente Docker, portanto não é necessário instalar o PostgreSQL diretamente na máquina para executar o projeto.

Durante a inicialização, o sistema também possui configurações para criação de dados iniciais, permitindo executar e testar a aplicação sem precisar cadastrar manualmente todos os dados necessários.

---

## Testes

O projeto possui testes automatizados utilizando as ferramentas de testes do ecossistema Spring.

Atualmente, alguns endpoints e funcionalidades já possuem testes automatizados.

A cobertura de testes será expandida conforme novas funcionalidades forem implementadas.

Para executar os testes através do Maven:

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```

---

## Roadmap

O projeto está em desenvolvimento e novas funcionalidades serão adicionadas gradualmente.

### Backend

* [x] Estrutura inicial do projeto
* [x] Integração com PostgreSQL
* [x] Docker / Docker Compose
* [x] Configuração de dados iniciais
* [x] Autenticação
* [x] Usuários
* [x] Clientes
* [x] Produtos
* [x] Controle de estoque
* [x] Movimentações de estoque
* [ ] Expansão dos testes automatizados
* [ ] Melhorias na documentação da API
* [ ] Expansão dos módulos do ERP

### Frontend

* [x] Interface inicial com Thymeleaf
* [ ] Melhorias na interface
* [ ] Desenvolvimento de API REST completa
* [ ] Frontend independente
* [ ] Migração para React ou outra tecnologia adequada

### Infraestrutura

* [x] Docker
* [x] Docker Compose
* [ ] CI/CD
* [ ] Melhorias no ambiente de produção
* [ ] Monitoramento e observabilidade

---

## 🤝 Contribuindo

O projeto está aberto para contribuições.

Se você quiser ajudar no desenvolvimento, pode:

* Encontrar e reportar bugs;
* Sugerir novas funcionalidades;
* Melhorar a arquitetura;
* Criar ou melhorar testes;
* Melhorar a documentação;
* Desenvolver novas funcionalidades;
* Sugerir melhorias no frontend;
* Abrir Pull Requests.

Se quiser participar diretamente do desenvolvimento, fique à vontade para entrar em contato.

---

## Status do projeto

**Em desenvolvimento**

Este projeto está sendo desenvolvido gradualmente e algumas funcionalidades ainda estão em implementação.

Mudanças na arquitetura, tecnologias e organização do projeto podem acontecer conforme o desenvolvimento evolui.

---

Obrigado por visitar o projeto!, sim ela ajudou a gerar esse readme :(
