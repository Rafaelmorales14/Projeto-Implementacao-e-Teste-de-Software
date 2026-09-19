package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {
    @Test
    void deveAprovarOuRecusarNaPrimeiraTentativa() {
        List<Long> cobrancas = new ArrayList<>();
        PagamentoService aprovado = new PagamentoService(total -> { cobrancas.add(total); return true; });
        PagamentoService recusado = new PagamentoService(total -> false);
        assertAll(() -> assertTrue(aprovado.pagar(123, 1)), () -> assertEquals(List.of(123L), cobrancas),
                () -> assertFalse(recusado.pagar(123, 3)));
    }

    @Test
    void deveRepetirAposIndisponibilidadeEAprovar() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> {
            chamadas[0]++;
            if (chamadas[0] < 3) throw new IllegalStateException();
            return true;
        });
        assertAll(() -> assertTrue(service.pagar(500, 3)), () -> assertEquals(3, chamadas[0]));
    }

    @Test
    void deveRecusarAposEsgotarTentativas() {
        int[] chamadas = {0};
        PagamentoService service = new PagamentoService(total -> { chamadas[0]++; throw new IllegalStateException(); });
        assertAll(() -> assertFalse(service.pagar(500, 2)), () -> assertEquals(2, chamadas[0]));
    }

    @Test
    void devePropagarExcecaoDiferenteDeIndisponibilidade() {
        PagamentoService service = new PagamentoService(total -> { throw new UnsupportedOperationException(); });
        assertThrows(UnsupportedOperationException.class, () -> service.pagar(1, 1));
    }

    @Test
    void deveValidarDependenciaTotalELimite() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new PagamentoService(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> new PagamentoService(total -> true).pagar(0, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new PagamentoService(total -> true).pagar(1, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new PagamentoService(total -> true).pagar(1, 4)));
    }
}
