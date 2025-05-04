# Linketinder

O **Linketinder** é uma aplicação que simula um sistema de cadastro e busca para candidatos e empresas. A ideia é fazer com que candidatos e empresas possam se cadastrar no sistema e visualizar os outros registros cadastrados.

## Funcionalidades

### 1. 🚀 API RESTful

   - CRUD completo para Candidatos, Empresas e Vagas;

   - Validação de dados integrada;

   - Respostas em JSON padronizadas;

   - Status HTTP semânticos.

### 2. 👨‍💻 Para Candidatos

   - Cadastro com competências;

   - Busca por ID ou listagem completa;

   - Atualização de perfil;

   - Exclusão de conta.

### 3. 🏢 Para Empresas

   - Cadastro com dados corporativos;

   - Publicação de vagas;

   - Gestão de oportunidades.

### 4. 🔍 Vagas

   - Criação por empresas;

   - Listagem filtrada;
   - 
   - Atualização de status.
     
## Como Usar

### Instruções:

1. Clone o repositório ou baixe o projeto.
2. Compile e execute o código no seu IDE ou via terminal. (Versão backend)
   2.1. O menu principal será exibido. Selecione uma opção:
   - **1** para cadastrar um candidato.
   - **2** para cadastrar uma empresa.
   - **3** para listar candidatos cadastrados.
   - **4** para listar empresas cadastradas.
   - **5** para sair da aplicação.
3. Use o vite para rodar o servidor localmente. (Versão frontend)
   3.1.: Uma tela de menu será exibida. Você pode escolher entre cadastrar uma empresa, cadastrar um candidato, olhar os perfis de candidatos disponíveis e olhar as vagas disponíveis. Ao olhar os candidatos disponíveis, um gráfico é apresentado, representando as competências escolhidas pelos candidatos.

## Contribuição

Se você deseja contribuir para o desenvolvimento deste projeto, siga os passos abaixo:

1. Fork este repositório.
2. Crie uma branch para suas alterações (`git checkout -b feature/nova-feature`).
3. Commit suas alterações (`git commit -am 'Adiciona nova feature'`).
4. Push para a branch (`git push origin feature/nova-feature`).
5. Abra um pull request.

### MySQL

Na feature mais recente, comecei a trabalhar com banco de dados. Ainda não foi integrado completamente, mas será em features futuras.

### MySQL Workbench

Aqui uma demonstração de como me planejei e organizei o MySQL

![alt text](image.png)

## Documentação da API

- Endpoints Base: http://localhost:8080/linketinder/api/

### Como Usar a API

#### Pré-requisitos

   - Java 11+

   - PostgreSQL 12+

   - Tomcat 9+
     
#### Instalação

**1.** Clone o repositório: 
```
git clone https://github.com/ppcostaa/Linketinder-Project.git
```
**2.** Configure o banco de dados:
   - Execute o script SQL em src/main/resources/schema.sql
   - Configure as credenciais em .env
**3.** Inicie a aplicação:
```
./gradlew clean war
cp build/libs/linketinder.war $TOMCAT_HOME/webapps/
```
**4.** Testando endpoints:
 - Use curl ou Postman para testar!

### Autora

Gio PCosta
