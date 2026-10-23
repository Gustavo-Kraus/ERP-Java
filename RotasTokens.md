# Tokens de acesso à API

Este documento descreve a emissão, o uso e as regras dos tokens implementadas
em `Controller/Tokens`, `Service/Tokens` e `Config/ApiTokenAuthenticationFilter`.

## Emitir token pela API

### `POST /api/tokens`

Esta rota é pública e recebe as credenciais do usuário no corpo JSON:

```json
{
  "email": "usuario@example.com",
  "senha": "sua-senha",
  "duracao": "DIAS_30"
}
```

Os campos `email`, `senha` e `duracao` são obrigatórios. Para compatibilidade,
`usuario` e `username` também podem ser usados no lugar de `email`, e
`password` no lugar de `senha`.

Valores aceitos para `duracao`:

| Valor | Validade |
| --- | --- |
| `DIAS_30` | 30 dias |
| `DIAS_60` | 60 dias |
| `DIAS_120` | 120 dias |
| `PERMANENTE` | Sem data de expiração |

Resposta de sucesso: HTTP `201 Created`.

```json
{
  "token": "valor-do-token",
  "expiraEm": "2026-11-21T22:30:00"
}
```

Para `PERMANENTE`, `expiraEm` é `null`. Se faltar algum campo obrigatório, a
resposta é HTTP `400`:

```json
{
  "erro": "Informe email, senha e duração do token."
}
```

Credenciais inválidas retornam HTTP `401`:

```json
{
  "erro": "Credenciais inválidas."
}
```

O token é associado ao usuário autenticado e é gerado com 32 bytes aleatórios,
codificados em Base64 URL-safe sem preenchimento (`=`).

## Emitir token pela interface web

### `GET /tokens`

Exibe a página de gerenciamento com os tokens associados ao usuário
autenticado.

### `POST /tokens/gerar`

Gera um token permanente para o usuário autenticado, exibe o valor recém-criado
na página e redireciona para `/tokens`. Esta ação é feita pela interface web
com autenticação de sessão.

## Enviar token nas chamadas da API

Para autenticar uma rota `/api/**`, envie o token em um dos cabeçalhos:

```http
Authorization: Bearer valor-do-token
```

ou:

```http
X-API-Token: valor-do-token
```

O filtro procura primeiro `Authorization` com o prefixo exato `Bearer ` e,
caso não encontre um token nesse formato, consulta `X-API-Token`. Espaços
extras ao redor do valor são removidos.

Quando o token existe, não foi revogado, ainda não expirou e pertence a um
usuário habilitado, esse usuário é usado como identidade autenticada na
requisição. Tokens expirados, revogados, desconhecidos ou pertencentes a
usuários desabilitados não autenticam a chamada. Requisições a `/api/**` sem
autenticação válida recebem HTTP `401`.

## Regras e observações

- A emissão via `POST /api/tokens` não exige token prévio; exige email e senha
  válidos de um usuário habilitado.
- O prazo é calculado em dias a partir do momento da emissão. `PERMANENTE`
  corresponde a `expiraEm: null`.
- Os tokens são armazenados associados ao usuário, com data de criação,
  expiração opcional e campo de revogação.
- A implementação atual não expõe uma rota para revogar tokens.
- A página `/tokens` gera tokens permanentes e lista os valores dos tokens
  existentes do usuário. Trate o token como uma credencial: não o compartilhe
  nem o inclua em código ou registros públicos.
