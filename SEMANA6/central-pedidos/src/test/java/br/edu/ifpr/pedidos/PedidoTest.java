package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {
    private ItemPedido item(String sku, int quantidade, int estoque, int peso, boolean fragil) {
        return new ItemPedido(sku, 1_000, quantidade, estoque, peso, fragil);
    }

    @Test
    void deveCalcularSubtotalPesoFragilidadeEEstoque() {
        Pedido pedido = new Pedido(List.of(item("INATIVO", 0, 0, 100, true), item("ATIVO", 2, 3, 700, false)), "PR", false, null);
        assertAll(() -> assertEquals(2_000, pedido.subtotalCentavos()), () -> assertEquals(1_400, pedido.pesoGramas()),
                () -> assertFalse(pedido.temFragil()), () -> assertTrue(pedido.estoqueSuficiente()));
    }

    @Test
    void deveReconhecerItemFragilAtivoEFaltaDeEstoque() {
        Pedido pedido = new Pedido(List.of(item("OK", 1, 1, 100, true), item("FALTA", 2, 1, 100, false)), "SP", false, null);
        assertAll(() -> assertTrue(pedido.temFragil()), () -> assertFalse(pedido.estoqueSuficiente()));
    }

    @Test
    void deveCopiarListaDefensivamente() {
        List<ItemPedido> itens = new ArrayList<>(List.of(item("A", 1, 1, 100, false)));
        Pedido pedido = new Pedido(itens, "PR", false, null);
        itens.clear();
        assertEquals(1_000, pedido.subtotalCentavos());
    }

    @Test
    void deveValidarListaEUf() {
        List<ItemPedido> muitos = new ArrayList<>();
        for (int i = 0; i < 101; i++) muitos.add(item("S" + i, 0, 0, 1, false));
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Pedido(null, "PR", false, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Pedido(muitos, "PR", false, null)),
                () -> assertThrows(NullPointerException.class, () -> new Pedido(List.of((ItemPedido) null), "PR", false, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Pedido(List.of(), null, false, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Pedido(List.of(), "P1", false, null)));
    }
}
