package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {
    private final AnaliseRisco risco = new AnaliseRisco();

    @Test
    void deveRecusarClienteBloqueado() {
        assertEquals("RECUSADO", risco.avaliar(new Cliente(false, true, 0), 1, false));
    }

    @Test
    void deveRevisarNovoClientePorTotalOuExpresso() {
        Cliente novo = new Cliente(false, false, 0);
        assertAll(() -> assertEquals("REVISAO", risco.avaliar(novo, 100_001, false)),
                () -> assertEquals("REVISAO", risco.avaliar(novo, 1, true)),
                () -> assertEquals("APROVADO", risco.avaliar(novo, 100_000, false)));
    }

    @Test
    void deveRevisarClienteSemVipComHistoricoPorTotalAlto() {
        assertAll(() -> assertEquals("REVISAO", risco.avaliar(new Cliente(false, false, 1), 500_001, false)),
                () -> assertEquals("APROVADO", risco.avaliar(new Cliente(true, false, 1), 500_001, false)),
                () -> assertEquals("APROVADO", risco.avaliar(new Cliente(false, false, 1), 500_000, false)));
    }

    @Test
    void deveRejeitarTotalNegativo() {
        assertThrows(IllegalArgumentException.class, () -> risco.avaliar(new Cliente(false, false, 1), -1, false));
    }
}
