# user-rest-api

## Microserviço para gerenciamento e autenticação de usuários
- CRUD de de usuários de sistema (id, name, email, password, address, phone, profile).
- Perfis de usuário: ADMIN e USER.
- Apenas usuários autenticados possuem acesso.
- Apenas usuários com perfil ADMIN podem criar, editar e deletar usuários. A pesquisa de usuários pode ser realizada pelos perfis ADMIN e USER.

### Rodando a aplicação
- Realizar build da imagem docker:
```
./gradlew buildDocker
```
- A aplicação está configurada para conectar em um banco de dados MongoDB rodando em um container separado, por isso é necessário rodar o container do banco:
```
docker run --name mongo -d -p 27017:27017 mongo:latest
```
- Rodar o container da aplicação:
```
docker run -p 8080:8080 --link=mongo -t frnndml/user-rest-api:0.1.0
```
- A aplicação estará acessível em http://localhost:8080.
- Junto com o código, foi disponibilizado um arquivo YAML para deployment em Kubernetes. O arquivo contempla apenas o microserviço, sendo necessário fornecer um deployment para o MongoDB.

### Acessando os serviços
- Quando a aplicação é iniciada, um novo usuário com perfil ADMIN é criado no banco:
```
username: admin@admin.com
password: 1234
```
- O username utilizado na autenticação é o e-mail do usuário.
  - Com o Postman, utilizar `Authorization` tipo `Basic Auth`, informando username e password nos respectivos campos.
  - Com o cURL, adicionar a opção para enviar usuário e senha:
    ```
    curl -X GET -u admin@admin.com:1234 http://localhost:8080/users 
    ```
- O serviços disponíveis são os seguintes:
  - Lista usuários:
    - GET http://localhost/users
    - GET http://localhost:8080/users?page=0&size=2
    - GET http://localhost:8080/users?page=0&size=10&sort=name,desc
  - Busca usuário por id:
    - GET http://localhost/users/{id}
  - Cadastra um novo usuário:
    - POST http://localhost/users
    ```
    {
    "name": "John Doe",
    "email": "john.doe@gmail.com",
    "password": "1234",
    "address": "Main St.",
    "phone": "555-0123",
    "profile": "USER"
    }
    ```
  - Edita um usuário:
    - PUT http://localhost/users
  - Remove um usuario
    - DELETE http://localhost/users/{id}
