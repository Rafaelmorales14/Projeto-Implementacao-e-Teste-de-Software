package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {
    private ItemPedido item(int quantidade, int estoque) {
        return new ItemPedido("SKU", 1_000, quantidade, estoque, 500, false);
    }

    @Test
    void deveCalcularTotalEDisponibilidade() {
        ItemPedido item = new ItemPedido("A", 333, 3, 3, 500, true);
        assertAll(() -> assertEquals(999, item.totalCentavos()), () -> assertTrue(item.disponivel()),
                () -> assertFalse(item(4, 3).disponivel()));
    }

    @Test
    void deveValidarSku() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido(null, 1, 0, 0, 1, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido(" ", 1, 0, 0, 1, false)));
    }

    @Test
    void deveValidarPrecoQuantidadeEstoqueEPeso() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido("A", 0, 0, 0, 1, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido("A", 1_000_001, 0, 0, 1, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido("A", 1, -1, 0, 1, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido("A", 1, 101, 0, 1, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido("A", 1, 0, -1, 1, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido("A", 1, 0, 0, 0, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> new ItemPedido("A", 1, 0, 0, 100_001, false)));
    }
}
