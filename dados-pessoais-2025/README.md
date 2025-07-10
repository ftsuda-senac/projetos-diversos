# Projeto Dados Pessoais 2025

Projeto de referência para demonstrar recursos básicos do Spring Boot

* Versão do Spring Boot: 3.5.3
* Versão do Java: 21
* Banco de dados: H2

## Objetivo

Reimplementar o projeto dados-pessoais trazendo boas práticas que não foram usadas no projeto anterior

* Uso do DTO separado do Entity
* Parâmetros de busca/paginação agrupados em objeto
* Documentação Swagger das APIs REST
* Tratamento de erros centralizado + Problem Details

## Processo de desenvolvimento

- [x] Wireframe das telas
    * Disponíveis como arquivos estáticos
- [x] Modelar entity/BD
    - Pessoas
        - ID interno
        - UUID publico
        - Username
        - Nome completo
        - E-mail
        - Telefone
        - Senha
        - Data de cadastro
        - Status ativo/inativo
    - Interesses
        - ID
        - Nome
    - Foto pessoa
        - ID
        - Nome arquivo
        - Nome arquivo original
        - Legenda
- [x] Criar services
- [x] Criar Webservices REST
    - [x] CRUD básico
    - [x] Upload de fotos
- [ ] Configurações e implementações de Security
    - [ ] Geração/leitura do token JWT
    - [ ] Login
    - [ ] Alterar senha
    - [ ] Resetar senha
    - [ ] Configurar autorizações de acesso
    - [ ] Obter usuário do token
- [x] Implementar versão MVC clássico com Thymeleaf
- [x] Testes automatizados
    - [x] Data JPA
    - [x] Service (somente 1 funcionalidade)
    - [x] RestController (somente 1 funcionalidade)
    - [ ] MvcController
    - [x] Integração (somente 1 funcionalidade)


## Dependências Spring Initializr

* Web
* Thymeleaf
* Validation
* Data JPA
* H2
* Security
* Lombok
* Configuration Processor
* Devtools
* JSpecify

https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.5.3&packaging=jar&jvmVersion=21&groupId=br.com.webmobi&artifactId=dados-pessoais&name=dados-pessoais&description=Exemplo%20projeto%20dados%20pessoais&packageName=br.com.webmobi.dadospessoais&dependencies=devtools,lombok,configuration-processor,web,data-jpa,security,validation,thymeleaf,h2

Após abrir o projeto na IDE, adicionar manualmente as seguintes dependências no pom.xml

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.9</version>
</dependency>
<dependency>
    <groupId>nz.net.ultraq.thymeleaf</groupId>
    <artifactId>thymeleaf-layout-dialect</artifactId>
    <version>3.4.0</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
</dependency>
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>webjars-locator-lite</artifactId>
</dependency>
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>bootstrap</artifactId>
    <version>5.3.7</version>
</dependency>
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>font-awesome</artifactId>
    <version>6.7.2</version>
</dependency>
```

Adicionar o arquivo .editorconfig com o conteúdo abaixo:
```
[*]
end_of_line = lf
insert_final_newline = true
trim_trailing_whitespace = true
charset = utf-8

[*.java]
indent_style = tab
indent_size = 4

[{*.js, *.jsx, *.ts, *.tsx}]
indent_style = space
indent_size = 2

[*.html]
indent_style = space
indent_size = 2

[{*.css, *.scss}]
indent_style = space
indent_size = 2

[*.xml]
indent_style = tab
indent_size = 4

[{*.yml, *.yaml}]
indent_style = space
indent_size = 4
```
