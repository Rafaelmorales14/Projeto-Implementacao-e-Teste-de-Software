package br.edu.ifpr.pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {
    private final CalculadoraFrete frete = new CalculadoraFrete();

    private Pedido pedido(String uf, boolean expresso, int peso, boolean fragil) {
        ItemPedido item = new ItemPedido("A", 1_000, 1, 1, peso, fragil);
        return new Pedido(List.of(item), uf, expresso, null);
    }

    @Test
    void deveAplicarTarifasBasePorUf() {
        Cliente comum = new Cliente(false, false, 1);
        assertAll(() -> assertEquals(1_200, frete.calcular(pedido("PR", false, 2_000, false), comum, 1)),
                () -> assertEquals(2_000, frete.calcular(pedido("SP", false, 2_000, false), comum, 1)),
                () -> assertEquals(2_000, frete.calcular(pedido("RJ", false, 2_000, false), comum, 1)),
                () -> assertEquals(3_000, frete.calcular(pedido("MG", false, 2_000, false), comum, 1)));
    }

    @Test
    void deveCobrarExcedenteComUmaEMultiplasIteracoes() {
        Cliente comum = new Cliente(false, false, 1);
        assertAll(() -> assertEquals(1_500, frete.calcular(pedido("PR", false, 2_001, false), comum, 1)),
                () -> assertEquals(1_800, frete.calcular(pedido("PR", false, 3_001, false), comum, 1)));
    }

    @Test
    void deveZerarFreteParaValorLiquidoAltoENaoExpresso() {
        assertEquals(0, frete.calcular(pedido("PR", false, 3_001, false), new Cliente(false, false, 1), 30_000));
    }

    @Test
    void deveManterBaseQuandoValorAltoEModoExpresso() {
        assertEquals(2_700, frete.calcular(pedido("PR", true, 2_000, false), new Cliente(false, false, 1), 30_000));
    }

    @Test
    void deveAplicarMetadeVipEAdicionais() {
        assertEquals(2_600, frete.calcular(pedido("PR", true, 2_000, true), new Cliente(true, false, 1), 1));
    }

    @Test
    void deveRejeitarLiquidoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> frete.calcular(pedido("PR", false, 1, false), new Cliente(false, false, 1), -1));
    }
}
