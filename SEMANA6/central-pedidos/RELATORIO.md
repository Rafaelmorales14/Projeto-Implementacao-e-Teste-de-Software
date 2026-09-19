# Relatório — Central de pedidos

## Modelo adotado

Os CFGs usam uma entrada e uma saída unificada. Cada operando de `&&` e `||` é uma decisão, pois o operando direito pode não ser avaliado. Retornos e `throw` apontam para a saída. No `switch`, PR, SP/RJ e `default` são saídas distintas. A aresta do `catch` de pagamento foi desenhada, mas não é um branch contado pelo JaCoCo.

```text
PedidoService.fechar
 ├─ Pedido.subtotalCentavos / estoqueSuficiente
 ├─ PoliticaDesconto.calcular
 ├─ CalculadoraFrete.calcular
 ├─ AnaliseRisco.avaliar
 └─ PagamentoService.pagar ──> ProcessadorPagamento.autorizar
```

## CFGs, McCabe e base de caminhos

| Método | CFG resumido | Nós | Arestas | V(G) = E − N + 2 | Base de caminhos independentes |
| --- | --- | ---: | ---: | ---: | --- |
| `PoliticaDesconto.calcular` | negativo? → VIP? → subtotal ≥ 50.000? → cupom nulo/branco? → `switch` → elegibilidade → teto? | 23 | 34 | 13 | negativo; VIP; comum no limite/abaixo; branco; BEMVINDO elegível, por subtotal baixo, por histórico e limitado; EXTRA10 elegível/abaixo/VIP; desconhecido |
| `CalculadoraFrete.calcular` | negativo? → `switch` UF → `while` peso → gratuidade → VIP? → expresso? → frágil? | 17 | 25 | 10 | negativo; PR sem excesso; SP; RJ; padrão; uma e várias iterações; gratuidade; expresso; VIP+expresso+frágil |
| `AnaliseRisco.avaliar` | negativo? → bloqueado? → sem histórico? → total alto? → expresso? → recorrente alto e não VIP? | 14 | 20 | 8 | negativo; bloqueado; novo por total/expresso/aprovado; recorrente alto não VIP; recorrente VIP; recorrente no limite |
| `PagamentoService.pagar` | total válido? → limites? → `do` autorizar → retorno ou `catch` → tentativa < limite? | 11 | 15 | 6 | total inválido; limite 0/4; aprova; recusa; indisponível e aprova; esgota; outra exceção propaga |
| `PedidoService.fechar` | referências → bloqueado? → subtotal zero? → estoque? → desconto/frete/risco → aprovado? → pagamento? | 15 | 19 | 6 | nulo; bloqueado; sem item ativo; sem estoque; revisão; pago; pagamento recusado |

Os dois caminhos de pagamento por indisponibilidade compartilham as decisões até o `catch`, mas são comportamentalmente distintos: um aprova após repetir e outro esgota tentativas. Ambos foram testados.

## Matriz de testes

| ID / método JUnit | Unidade | Entrada e estado | Resultado esperado | Caminho / critério |
| --- | --- | --- | --- | --- |
| `ClienteTest` | Cliente | histórico válido e -1 | atributos; exceção | validação |
| `ItemPedidoTest` | Item | total, estoque e limites inválidos | 999, disponível/indisponível; exceções | métodos e `||` |
| `PedidoTest.deveCalcularSubtotalPesoFragilidadeEEstoque` | Pedido | linha inativa + ativa | subtotal 2.000; peso 1.400 | `for` e `continue` |
| `PedidoTest.deveReconhecerItemFragilAtivoEFaltaDeEstoque` | Pedido | frágil ativo; falta na segunda linha | `true` e `false` | retorno e `break` |
| `PedidoTest.deveCopiarListaDefensivamente` | Pedido | lista alterada depois da construção | subtotal 1.000 | cópia defensiva |
| `PedidoTest.deveValidarListaEUf` | Pedido | lista/UF inválidas | exceções | curto-circuito |
| `PoliticaDescontoTest.deveAplicarDescontosBase` | Desconto | VIP; comum em 50.000 e 49.999 | 10%, 5%, 0 | limites |
| `PoliticaDescontoTest.deveNormalizarBemVindoEAplicarTeto` | Desconto | cupom normalizado; subtotal/histórico | 2.000, 0, 0 e teto 2.000 | BEMVINDO e `&&` |
| `PoliticaDescontoTest.deveAplicarExtra10QuandoElegivel` | Desconto | 20.000, 19.999 e VIP | 2.000, 0, 20.000 | limite EXTRA10 |
| `PoliticaDescontoTest.deveRejeitarSubtotalNegativoECupomDesconhecido` | Desconto | -1; cupom OUTRO | exceções | `default` |
| `CalculadoraFreteTest.deveAplicarTarifasBasePorUf` | Frete | PR, SP, RJ, MG | 1.200, 2.000, 2.000, 3.000 | todos os casos do `switch` |
| `CalculadoraFreteTest.deveCobrarExcedenteComUmaEMultiplasIteracoes` | Frete | 2.001 e 3.001 g | 1.500 e 1.800 | `while` 1 e 2 vezes |
| demais `CalculadoraFreteTest` | Frete | líquido 30.000 normal/expresso; VIP; frágil; -1 | gratuidade, adicionais, exceção | decisões independentes |
| `AnaliseRiscoTest` | Risco | bloqueado; novo; recorrente VIP/não VIP; -1 | RECUSADO, REVISAO, APROVADO, exceção | retornos e `||`/`&&` |
| `PagamentoServiceTest` | Pagamento | stubs true/false/indisponível/outra exceção | valor e quantidade de chamadas | `do/while`, `catch` e propagação |
| `PedidoServiceTest` | Serviço | bloqueado, inativo, sem estoque, revisão, pago, recusa e nulos | status/valores; nenhuma cobrança antecipada | todos os retornos de `fechar` |

## Evolução da cobertura

| Etapa | Testes executados | Linhas | Branches | Métodos | Classes | Lacunas |
| --- | ---: | ---: | ---: | ---: | ---: | --- |
| Inicial | 1 exemplo | Não medido | Não medido | Não medido | Não medido | Apenas o caminho pago comum |
| Final (`mvn clean test`) | 35 | 108/108 (100%) | 116/116 (100%) | 21/21 (100%) | 9/9 (100%) | Nenhuma alcançável |

O relatório JaCoCo foi gerado em `target/site/jacoco/index.html`.

## Análise crítica

Cobrir todos os ramos não cobre todos os caminhos. Em `CalculadoraFrete`, os resultados verdadeiro/falso de gratuidade, VIP, expresso e frágil podem ser cobertos isoladamente sem exercitar, por exemplo, “gratuidade + VIP + expresso + frágil”. As combinações multiplicam caminhos; por isso foram incluídos casos de decisões independentes, não só de ramos isolados.

Os curto-circuitos foram verificados. Para `BEMVINDO`, há cliente com histórico (o subtotal não é avaliado), sem histórico e subtotal 9.999, e sem histórico/subtotal 10.000. Em risco, total alto e expresso distinguem os dois lados de `||`.

Exceções não aparecem como branches no JaCoCo. `PagamentoService` foi testado com indisponibilidade seguida de sucesso, esgotamento e `UnsupportedOperationException` propagada, verificando também a quantidade de tentativas. Um caminho viável em `AnaliseRisco` — bloqueado retorna `RECUSADO` — é inviável via serviço, porque `fechar` devolve `BLOQUEADO` antes de chamar o risco.

Como mutação manual sugerida, alterar temporariamente `subtotal >= 50_000` para `subtotal > 50_000` faz `deveAplicarDescontosBase` falhar no limite. A alteração foi desfeita: nenhuma regra de produção foi modificada.
