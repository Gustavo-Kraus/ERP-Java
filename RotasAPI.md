# Rotas da API

As rotas abaixo são disponibilizadas pelos controladores em `Controller/API`.
Os exemplos mostram o formato JSON e usam valores ilustrativos. Campos de data
e campos não preenchidos podem ser retornados como `null`.

## Clientes

### `GET /api/clientes/listar`

Lista clientes. A resposta é uma lista de `ClienteResponse`:

```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "nomeFantasia": null,
    "razaoSocial": null,
    "cnpjCpf": null,
    "telefone": null,
    "celular": null,
    "email": "joao@example.com",
    "cep": null,
    "rua": null,
    "bairro": null,
    "numeroCasa": null,
    "criadoEm": "2026-01-15T10:30:00",
    "editadoEm": null,
    "excluidoEm": null
  }
]
```

Se o limite de requisições for excedido, retorna HTTP `429` com uma mensagem
em texto.

### `GET /api/clientes/listar/{id}`

Busca um cliente pelo ID. O parâmetro de consulta opcional `busca` é aceito,
mas não é usado nesta operação. Retorna um objeto `Clientes` com os campos
`id`, `nome`, `nomeFantasia`, `razaoSocial`, `cnpjCpf`, `telefone`, `celular`,
`email`, `cep`, `rua`, `bairro`, `numeroCasa`, `criadoEm`, `editadoEm` e
`excluidoEm`.

### `POST /api/clientes/salvar`

Corpo da requisição:

```json
{
  "nome": "João Silva",
  "email": "joao@example.com"
}
```

Retorna o cliente salvo como objeto `Clientes`, com os campos descritos em
`GET /api/clientes/listar/{id}`.

### `DELETE /api/clientes/apagar/{id}`

Marca o cliente como excluído e retorna uma mensagem em texto, por exemplo:

```text
Cliente Apagado com sucesso 1
```

### `POST /api/clientes/atualizar/{id}`

Corpo da requisição:

```json
{
  "nome": "João Silva",
  "email": "joao.novo@example.com"
}
```

Retorna o cliente atualizado como objeto `Clientes`.

## Produtos

### `GET /api/produtos/listar`

Lista até seis produtos ativos. Aceita o parâmetro opcional `busca` para
filtrar os resultados. Retorna uma lista de objetos `Produtos`.

### `GET /api/produtos/listar/{id}`

Busca um produto pelo ID e retorna um objeto `Produtos`. O parâmetro opcional
`busca` é aceito, mas não é usado nesta operação.

### `POST /api/produtos/salvar`

Corpo da requisição:

```json
{
  "codigo": "PROD-001",
  "codigoBarras": "7890000000010",
  "nome": "Produto exemplo",
  "descricao": "Descrição do produto",
  "unidadeMedida": "UNIDADE",
  "tipoProduto": "PRODUTO"
}
```

Retorna o produto salvo como objeto `Produtos`. O objeto contém os campos
`id`, `codigo`, `codigoBarras`, `nome`, `descricao`, `unidadeMedida`,
`tipoProduto`, `categoria`, `marca`, `modelo`, `fabricante`,
`referenciaFabricante`, `ncm`, `cest`, `origemMercadoria`, `cfopVenda`,
`cfopCompra`, `cstIcms`, `csosn`, `cstPis`, `cstCofins`, `cstIpi`,
`aliquotaIcms`, `aliquotaPis`, `aliquotaCofins`, `aliquotaIpi`, `ativo`,
`controlaEstoque`, `aceitaQuantidadeFracionada`, `permiteEstoqueNegativo`,
`exigeLote`, `exigeNumeroSerie`, `criadoEm`, `atualizadoEm`,
`desativadoEm`, `pesoLiquido`, `pesoBruto`, `altura`, `largura`,
`comprimento` e `volume`. `categoria` e `marca` são objetos relacionados.

### `DELETE /api/produtos/apagar/{id}`

Desativa o produto e retorna uma mensagem em texto:

```text
Produto apagado com sucesso
```

## Estoque

### `POST /api/estoque/salvar`

Corpo da requisição:

```json
{
  "produto": 1,
  "almoxarifado": 1,
  "quantidadeAtual": 10,
  "quantidadeReservada": 0,
  "estoqueMinimo": 2,
  "estoqueMaximo": 100,
  "pontoReposicao": 5,
  "precoCusto": 10.00,
  "precoVenda": 15.00,
  "atualizar": null
}
```

Retorna o objeto `Estoque` salvo, contendo `id`, `produto`,
`almoxarifado`, `quantidadeAtual`, `quantidadeReservada`, `estoqueMinimo`,
`estoqueMaximo`, `pontoReposicao`, `precoCusto`, `precoVenda`, `criadoEm` e
`atualizadoEm`. `produto` e `almoxarifado` são objetos relacionados.

### `POST /api/estoque/atualizar/{id}`

Recebe os mesmos campos de `POST /api/estoque/salvar` e retorna o objeto
`Estoque` atualizado.

### `POST /api/estoque/atualizarqtd`

Corpo da requisição:

```json
{
  "id": 1,
  "quantidade": 2,
  "metodo": "ENTRADA"
}
```

`metodo` deve ser `ENTRADA` ou `SAIDA`. Retorna o objeto `Estoque` atualizado.

### `POST /api/estoque/transferencia`

Corpo da requisição:

```json
{
  "idEstoqueOrigem": 1,
  "idEstoqueDestino": 2,
  "quantidade": 3
}
```

Retorna o objeto `Estoque` do almoxarifado de destino após a transferência.

### `GET /api/estoque/listar`

Retorna uma lista de `EstoqueResponse`:

```json
[
  {
    "id": 1,
    "quantidadeAtual": 10,
    "quantidadeReservada": 0,
    "estoqueMinimo": 2,
    "estoqueMaximo": 100,
    "pontoReposicao": 5,
    "precoCusto": 10.00,
    "precoVenda": 15.00,
    "criadoEm": "2026-01-15T10:30:00",
    "atualizadoEm": "2026-01-15T10:30:00"
  }
]
```

## Pedidos

### `POST /api/pedidos/salvar`

Corpo da requisição:

```json
{
  "clienteId": 1,
  "itens": [
    {
      "estoqueId": 1,
      "quantidade": 2,
      "desconto": 0,
      "precoVenda": 15.00
    }
  ],
  "desconto": 0,
  "acrescimo": 0,
  "observacao": "Pedido de exemplo"
}
```

Retorna HTTP `201 Created` e uma mensagem em texto com o ID criado:

```text
Pedido criado com sucesso. ID: 1
```

### `GET /api/pedidos` e `GET /api/pedidos/listar`

As duas rotas listam os pedidos da filial do usuário autenticado e retornam
uma lista de `PedidoResponse`:

```json
[
  {
    "id": 1,
    "numero": 1,
    "clienteId": 1,
    "status": "RASCUNHO",
    "subtotal": 30.00,
    "desconto": 0,
    "acrescimo": 0,
    "total": 30.00,
    "observacao": "Pedido de exemplo",
    "dataCriacao": "2026-01-15T10:30:00",
    "dataAtualizacao": "2026-01-15T10:30:00",
    "itens": [
      {
        "estoqueId": 1,
        "produtoId": 1,
        "quantidade": 2,
        "valorUnitario": 15.00,
        "desconto": 0,
        "total": 30.00
      }
    ]
  }
]
```

### `PUT /api/pedidos/{id}`

Recebe o mesmo corpo de `POST /api/pedidos/salvar` e retorna o pedido
atualizado como objeto `PedidoResponse`.

### `DELETE /api/pedidos/apagar/{id}`

Exclui o pedido e retorna HTTP `204 No Content`, sem corpo de resposta.

## Respostas de erro

Erros de conflito tratados pela API retornam HTTP `409` no formato
`{"erro": "mensagem"}`. Erros de requisição inválida tratados pela API
retornam HTTP `400` no mesmo formato.
