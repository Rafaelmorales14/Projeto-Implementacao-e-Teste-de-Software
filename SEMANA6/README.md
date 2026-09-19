# Semana 6 — Testes Estruturais

> Entrega da disciplina **Projeto, Implementação e Teste de Software**.

Nesta semana, o foco foi criar testes unitários com **JUnit 5** e analisar a cobertura estrutural com **JaCoCo**. As atividades foram desenvolvidas sem alterar as regras de produção.

## Projetos entregues

| Projeto | Objetivo | Resultado |
| --- | --- | --- |
| `boletim-simples` | Exercitar testes de média, classificação, laços e combinações booleanas. | 11 testes, 100% de linhas, branches, métodos e classes. |
| `central-pedidos` | Aplicar teste estrutural em um domínio orientado a objetos com descontos, frete, risco e pagamento. | 35 testes, 100% de linhas, branches, métodos e classes. |

## O que foi praticado

- Limites de decisão, como médias 4 e 7 e valores mínimos de desconto.
- Laços com zero, uma e várias iterações.
- Curto-circuito em condições com `&&` e `||`.
- `switch`, retornos antecipados, exceções e stubs de pagamento.
- Cobertura de linhas, branches, métodos e classes com JaCoCo.
- Grafos de fluxo de controle, complexidade ciclomática de McCabe e caminhos independentes.

## Como executar

Entre na pasta de qualquer projeto e execute:

```bash
mvn clean test
```

Ao final, o relatório de cobertura estará em:

```text
target/site/jacoco/index.html
```

## Estrutura

```text
SEMANA6/
├── boletim-simples/
│   ├── src/main/java/
│   └── src/test/java/
└── central-pedidos/
    ├── src/main/java/
    ├── src/test/java/
    └── RELATORIO.md
```

No projeto `central-pedidos`, o arquivo `RELATORIO.md` reúne a matriz de testes, CFGs, cálculos de McCabe, caminhos básicos e a discussão sobre as limitações da cobertura de ramos.
