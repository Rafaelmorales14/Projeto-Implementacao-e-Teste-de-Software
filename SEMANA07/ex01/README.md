# Exercício 1 - Classificação de pedido

## Semana 7

Este README contém a resolução do Exercício 1 da Semana 7.

## Método analisado

```java
public String classificarPedido(
        double valor,
        boolean clienteVip,
        boolean pagamentoAprovado) {

    double desconto = 0;

    if (valor >= 500) {
        desconto = 10;
    }

    if (clienteVip) {
        desconto += 5;
    }

    if (!pagamentoAprovado) {
        return "PAGAMENTO RECUSADO";
    }

    double valorFinal = valor - (valor * desconto / 100);
    return "PEDIDO APROVADO: " + valorFinal;
}
```

## 1. Blocos básicos

| Nó | Bloco | Tipo |
| -- | ----- | ---- |
| 1 | `desconto = 0` | Início e inicialização |
| 2 | `valor >= 500?` | Decisão do primeiro `if` |
| 3 | `desconto = 10` | Desconto para pedido de valor alto |
| 4 | `clienteVip?` | Decisão do segundo `if` |
| 5 | `desconto += 5` | Desconto adicional para cliente VIP |
| 6 | `!pagamentoAprovado?` | Decisão do terceiro `if` |
| 7 | `return "PAGAMENTO RECUSADO"` | Saída antecipada |
| 8 | `valorFinal = valor - (valor * desconto / 100)` | Cálculo do valor final |
| 9 | `return "PEDIDO APROVADO: " + valorFinal` | Saída aprovada |
| 10 | `Fim` | Encerramento do método |

## 2. Decisões

- Nó 2: verifica se `valor >= 500`.
- Nó 4: verifica se `clienteVip` é verdadeiro.
- Nó 6: verifica se `!pagamentoAprovado` é verdadeiro.

No nó 6, o ramo verdadeiro significa que o pagamento foi recusado. Nesse caso,
o método retorna imediatamente e não executa o cálculo de `valorFinal`.

## 3. Contagem do grafo

- `N = 10` nós.
- `E = 12` arestas.
- Decisões: 3.

Pela primeira fórmula:

```text
V(G) = E - N + 2
V(G) = 12 - 10 + 2
V(G) = 4
```

Pela segunda fórmula:

```text
V(G) = número de decisões + 1
V(G) = 3 + 1
V(G) = 4
```

As duas fórmulas chegam ao mesmo resultado.

## 4. Caminhos independentes e testes

| Caminho | Sequência de nós | Entradas | Retorno esperado |
| ------- | ---------------- | -------- | ---------------- |
| P1 - pedido comum aprovado | `1-2(F)-4(F)-6(F)-8-9-10` | `valor=400`, `clienteVip=false`, `pagamentoAprovado=true` | `PEDIDO APROVADO: 400.0` |
| P2 - desconto por valor | `1-2(T)-3-4(F)-6(F)-8-9-10` | `valor=600`, `clienteVip=false`, `pagamentoAprovado=true` | `PEDIDO APROVADO: 540.0` |
| P3 - desconto VIP | `1-2(F)-4(T)-5-6(F)-8-9-10` | `valor=400`, `clienteVip=true`, `pagamentoAprovado=true` | `PEDIDO APROVADO: 380.0` |
| P4 - pagamento recusado | `1-2(F)-4(F)-6(T)-7-10` | `valor=400`, `clienteVip=false`, `pagamentoAprovado=false` | `PAGAMENTO RECUSADO` |

Teste adicional combinando os dois descontos:

```text
Entradas: valor=600, clienteVip=true, pagamentoAprovado=true
Retorno: PEDIDO APROVADO: 510.0
```

## 5. Questões para discussão

### Quantas combinações entre as três condições são possíveis?

Existem `2^3 = 8` combinações possíveis para os resultados das três condições:
`valor >= 500`, `clienteVip` e `pagamentoAprovado`. Embora `valor` possa assumir
infinitos valores numéricos, ele gera apenas dois resultados relevantes para o
fluxo: menor que 500 ou maior/igual a 500.

### O número de combinações possíveis é igual à complexidade ciclomática?

Não. As 8 combinações representam cenários de execução possíveis. Já a
complexidade ciclomática é `V(G) = 4` e representa a quantidade mínima de
caminhos linearmente independentes necessária para cobrir a estrutura do
controle de fluxo.

### Como o `return` dentro da terceira condição altera o grafo?

Ele cria uma saída antecipada. Quando `!pagamentoAprovado` é verdadeiro, o
fluxo segue do nó 6 para o nó 7 e, em seguida, para o nó 10. Os nós 8 e 9 são
ignorados.

### É possível executar o cálculo de `valorFinal` quando o pagamento não foi aprovado?

Não. Quando o pagamento não é aprovado, o `return "PAGAMENTO RECUSADO"`
encerra o método antes que o fluxo alcance o nó 8.
