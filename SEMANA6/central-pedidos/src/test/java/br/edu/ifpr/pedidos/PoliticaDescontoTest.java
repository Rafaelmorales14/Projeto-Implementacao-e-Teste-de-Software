package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {
    private final PoliticaDesconto politica = new PoliticaDesconto();

    @Test
    void deveAplicarDescontosBase() {
        assertAll(
                () -> assertEquals(10_001, politica.calcular(new Cliente(true, false, 1), 100_019, null)),
                () -> assertEquals(2_500, politica.calcular(new Cliente(false, false, 1), 50_000, " ")),
                () -> assertEquals(0, politica.calcular(new Cliente(false, false, 1), 49_999, null)));
    }

    @Test
    void deveNormalizarBemVindoEAplicarTeto() {
        assertAll(
                () -> assertEquals(2_000, politica.calcular(new Cliente(false, false, 0), 10_000, " bemvindo ")),
                () -> assertEquals(0, politica.calcular(new Cliente(false, false, 0), 9_999, "BEMVINDO")),
                () -> assertEquals(0, politica.calcular(new Cliente(false, false, 1), 10_000, "BEMVINDO")),
                () -> assertEquals(2_000, politica.calcular(new Cliente(true, false, 0), 10_000, "BEMVINDO")));
    }

    @Test
    void deveAplicarExtra10QuandoElegivel() {
        assertAll(
                () -> assertEquals(2_000, politica.calcular(new Cliente(false, false, 1), 20_000, "extra10")),
                () -> assertEquals(0, politica.calcular(new Cliente(false, false, 1), 19_999, "EXTRA10")),
                () -> assertEquals(20_000, politica.calcular(new Cliente(true, false, 1), 100_000, "EXTRA10")));
    }

    @Test
    void deveRejeitarSubtotalNegativoECupomDesconhecido() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> politica.calcular(new Cliente(false, false, 0), -1, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> politica.calcular(new Cliente(false, false, 0), 10_000, "OUTRO")));
    }
}
