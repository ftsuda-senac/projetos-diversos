# Projeto Dados Pessoais 2025

Projeto de referência para demonstrar recursos básicos do Spring Boot

* Versão do Spring Boot: 3.4.5
* Versão do Java: 21

## Processo de desenvolvimento

* Wireframe das telas
* Modelar entity/BD
    * Pessoas
        * ID interno
        * UUID publico
        * Username
        * Nome completo
        * E-mail
        * Telefone
        * Senha
        * Data de cadastro
        * Status ativo/inativo
    * Interesses
        * ID
        * Nome
    * Foto pessoa
        * ID
        * Nome arquivo
        * Legenda
* Criar services
* Criar MVC + Thymeleaf para CRUD administrativo
* Configurar login para acesso administrativo
* Criar webservices para CRUD dos usuários + telas simples estáticas para acesso
* Configurar login com JWT para acesso próprio

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

https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.4.5&packaging=jar&jvmVersion=21&groupId=br.com.webmobi&artifactId=dados-pessoais&name=dados-pessoais&description=Exemplo%20projeto%20dados%20pessoais&packageName=br.com.webmobi.dadospessoais&dependencies=devtools,lombok,configuration-processor,web,data-jpa,security,validation,thymeleaf,h2

Após abrir o projeto na IDE, adicionar manualmente as seguintes dependências no pom.xml

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.8</version>
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
    <version>5.3.5</version>
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
```
