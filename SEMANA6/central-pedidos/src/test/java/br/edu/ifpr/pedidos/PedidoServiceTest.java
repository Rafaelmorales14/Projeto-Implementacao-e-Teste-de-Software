package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    private ItemPedido item(String sku, long preco, int quantidade, int estoque, int peso, boolean fragil) {
        return new ItemPedido(sku, preco, quantidade, estoque, peso, fragil);
    }

    @Test
    void deveFecharPedidoDeClienteComumComFreteDoParanaEPagamentoAprovado() {
        // 1. Preparar: cliente comum, uma compra anterior e item disponível de R$ 100,00.
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO-JAVA", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        // Simula o pagamento e registra as cobranças, sem banco ou serviço externo.
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        // 2. Executar: percorrer um caminho completo do fechamento.
        ResultadoPedido resultado = service.fechar(pedido, cliente);

        // 3. Verificar: sem desconto; frete de R$ 12,00; total de R$ 112,00.
        assertAll(
            () -> assertEquals("PAGO", resultado.status()),
            () -> assertEquals(10_000L, resultado.subtotalCentavos()),
            () -> assertEquals(0L, resultado.descontoCentavos()),
            () -> assertEquals(1_200L, resultado.freteCentavos()),
            () -> assertEquals(11_200L, resultado.totalCentavos()),
            // A lista comprova uma única cobrança, com o valor correto.
            () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void deveBloquearSemAvaliarItensOuCobrar() {
        List<Long> cobrancas = new ArrayList<>();
        Pedido pedido = new Pedido(List.of(item("A", 10_000, 1, 1, 100, false)), "PR", false, "INVALIDO");
        ResultadoPedido resultado = new PedidoService(total -> { cobrancas.add(total); return true; })
                .fechar(pedido, new Cliente(false, true, 0));
        assertAll(() -> assertEquals("BLOQUEADO", resultado.status()), () -> assertEquals(0, resultado.totalCentavos()),
                () -> assertTrue(cobrancas.isEmpty()));
    }

    @Test
    void deveRejeitarPedidoSemItensAtivosSemCobrar() {
        List<Long> cobrancas = new ArrayList<>();
        Pedido pedido = new Pedido(List.of(item("A", 1_000, 0, 0, 100, false)), "PR", false, null);
        PedidoService service = new PedidoService(total -> { cobrancas.add(total); return true; });
        assertThrows(IllegalArgumentException.class, () -> service.fechar(pedido, new Cliente(false, false, 1)));
        assertTrue(cobrancas.isEmpty());
    }

    @Test
    void deveRetornarSemEstoqueAntesDeAvaliarCupomOuCobrar() {
        List<Long> cobrancas = new ArrayList<>();
        Pedido pedido = new Pedido(List.of(item("A", 1_000, 2, 1, 100, false)), "PR", false, "INVALIDO");
        ResultadoPedido resultado = new PedidoService(total -> { cobrancas.add(total); return true; })
                .fechar(pedido, new Cliente(false, false, 1));
        assertAll(() -> assertEquals("SEM_ESTOQUE", resultado.status()), () -> assertEquals(0, resultado.totalCentavos()),
                () -> assertTrue(cobrancas.isEmpty()));
    }

    @Test
    void deveEncaminharNovoClienteExpressoParaRevisaoSemCobrar() {
        List<Long> cobrancas = new ArrayList<>();
        Pedido pedido = new Pedido(List.of(item("A", 10_000, 1, 1, 100, false)), "PR", true, null);
        ResultadoPedido resultado = new PedidoService(total -> { cobrancas.add(total); return true; })
                .fechar(pedido, new Cliente(false, false, 0));
        assertAll(() -> assertEquals("REVISAO", resultado.status()), () -> assertEquals(12_700, resultado.totalCentavos()),
                () -> assertTrue(cobrancas.isEmpty()));
    }

    @Test
    void deveRetornarPagamentoRecusadoComValoresCalculados() {
        Pedido pedido = new Pedido(List.of(item("A", 20_000, 1, 1, 100, false)), "SP", false, "EXTRA10");
        ResultadoPedido resultado = new PedidoService(total -> false).fechar(pedido, new Cliente(false, false, 1));
        assertAll(() -> assertEquals("PAGAMENTO_RECUSADO", resultado.status()), () -> assertEquals(20_000, resultado.subtotalCentavos()),
                () -> assertEquals(2_000, resultado.descontoCentavos()), () -> assertEquals(2_000, resultado.freteCentavos()),
                () -> assertEquals(20_000, resultado.totalCentavos()));
    }

    @Test
    void deveValidarReferenciasDoFechamentoEDependencia() {
        Pedido pedido = new Pedido(List.of(item("A", 1_000, 1, 1, 100, false)), "PR", false, null);
        PedidoService service = new PedidoService(total -> true);
        assertAll(() -> assertThrows(NullPointerException.class, () -> new PedidoService(null)),
                () -> assertThrows(NullPointerException.class, () -> service.fechar(null, new Cliente(false, false, 1))),
                () -> assertThrows(NullPointerException.class, () -> service.fechar(pedido, null)));
    }
}
