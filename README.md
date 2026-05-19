# 📦 Produtos API

API REST de Cadastro de Produtos desenvolvida com Java 21 + Spring Boot 3.

## 🚀 Endpoints

| Método | Endpoint                        | Descrição                      |
|--------|---------------------------------|--------------------------------|
| GET    | `/api/produtos`                 | Lista todos os produtos        |
| GET    | `/api/produtos?categoria=X`     | Filtra por categoria           |
| GET    | `/api/produtos?nome=X`          | Busca por nome                 |
| GET    | `/api/produtos/{id}`            | Busca produto por ID           |
| POST   | `/api/produtos`                 | Cria novo produto              |
| PUT    | `/api/produtos/{id}`            | Atualiza produto               |
| DELETE | `/api/produtos/{id}`            | Remove produto                 |
| GET    | `/actuator/health`              | Health check                   |

## 📋 Exemplo de payload

```json
{
  "nome": "Notebook Dell",
  "descricao": "Notebook para desenvolvimento",
  "preco": 4500.00,
  "quantidadeEstoque": 10,
  "categoria": "Eletrônicos"
}
```

## ▶️ Rodando localmente

```bash
./mvnw spring-boot:run
```

## 🐳 Rodando com Docker

```bash
docker build -t produtos-api .
docker run -p 8080:8080 produtos-api
```

## 🧪 Rodando os testes

```bash
./mvnw test
```
