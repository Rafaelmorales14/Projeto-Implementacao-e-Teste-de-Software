# Exercício 2 - Análise de leituras de temperatura

## Semana 7

Este README contém a resolução do Exercício 2 da Semana 7.

## Método analisado

```java
public int contarAlertas(double[] temperaturas) {
	int alertas = 0;
	int i = 0;

	while (i < temperaturas.length) {
		if (temperaturas[i] < 0) {
			alertas += 2;
		} else if (temperaturas[i] > 35) {
			alertas++;
		}

		i++;
	}

	return alertas;
}
```

## 1. Blocos básicos

| Nó | Bloco | Tipo |
| --- | --- | --- |
| 1 | `alertas = 0; i = 0` | Inicialização |
| 2 | `i < temperaturas.length?` | Decisão do `while` |
| 3 | `temperaturas[i] < 0?` | Decisão do primeiro `if` |
| 4 | `alertas += 2` | Temperatura negativa |
| 5 | `temperaturas[i] > 35?` | Decisão do `else if` |
| 6 | `alertas++` | Temperatura superior a 35 |
| 7 | `i++` | Incremento e próxima leitura |
| 8 | `return alertas` | Saída do método |

## 2. Decisões

- Nó 2: o `while` decide se ainda existe uma leitura para processar.
- Nó 3: o primeiro `if` decide se a temperatura é negativa.
- Nó 5: o `else if` decide se a temperatura é superior a 35.

Quando a temperatura não é negativa e também não é superior a 35, ela fica
no intervalo normal de 0 a 35, inclusive.

## 3. Contagem do grafo

- `N = 8` nós.
- `E = 10` arestas.
- Decisões: 3.

Pela primeira fórmula:

```text
V(G) = E - N + 2
V(G) = 10 - 8 + 2
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

| Caminho | Sequência de nós | Vetor de entrada | Retorno |
| --- | --- | --- | --- |
| P1 - saída sem iteração | `1-2(F)-8` | `[]` | `0` |
| P2 - temperatura negativa | `1-2(T)-3(T)-4-7-2(F)-8` | `[-2]` | `2` |
| P3 - temperatura superior a 35 | `1-2(T)-3(F)-5(T)-6-7-2(F)-8` | `[40]` | `1` |
| P4 - temperatura entre 0 e 35 | `1-2(T)-3(F)-5(F)-7-2(F)-8` | `[20]` | `0` |

Teste adicional das fronteiras:

```text
Entrada: [0, 35]
Retorno: 0
```

Os valores `0` e `35` passam pelo ramo normal porque as condições usam ` < 0`
e ` > 35`, sem incluir as fronteiras.

## 5. Questões para discussão

### Um vetor com várias temperaturas percorre um único caminho?

Não. Cada posição do vetor pode seguir um ramo diferente. Depois de cada
leitura, o nó 7 incrementa `i` e retorna ao nó 2. Portanto, partes do grafo
podem ser repetidas até o fim do vetor.

### Qual entrada permite sair sem acessar uma posição do vetor?

O vetor vazio `[]`. Nesse caso, a condição do `while` já começa falsa e o
método vai diretamente para o retorno.

### Por que testar 0 e 35?

Porque são valores de fronteira da classificação normal. Eles verificam se os
operadores estritos foram interpretados corretamente.

### Por que o `else if` é uma nova decisão?

Porque ele contém uma condição própria, com saída verdadeira para o nó 6 e
saída falsa para o nó 7.

### Por que o retorno do laço aparece no CFG?

A aresta do nó 7 para o nó 2 representa a próxima iteração. Sem essa aresta,
o grafo não mostraria que o incremento de `i` leva novamente à condição do
`while`, nem representaria corretamente a repetição do laço.
