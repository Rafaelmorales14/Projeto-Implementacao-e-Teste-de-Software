package br.edu.ifpr.boletim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoletimTest {

    @Test
    void deveAprovarAlunoNoLimiteSete() {
        // Preparar: criar o objeto que será testado.
        Boletim boletim = new Boletim();

        // Executar: chamar um único método com uma entrada conhecida.
        String resultado = boletim.verificarSituacao(7);

        // Verificar: comparar o resultado esperado com o resultado obtido.
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveRecuperarNotaAlunoComMediaQuatro() {
        Boletim boletim = new Boletim();

        // Executar: chamar um único método com uma entrada conhecida.
        String resultado = boletim.verificarSituacao(4);

        // Verificar: comparar o resultado esperado com o resultado obtido.
        assertEquals("RECUPERACAO", resultado);
    }

    @Test
    void deveReprovarAlunoAbaixoDoLimiteQuatro() {
        Boletim boletim = new Boletim();

        // Executar: chamar um único método com uma entrada conhecida.
        String resultado = boletim.verificarSituacao(3.99);

        // Verificar: comparar o resultado esperado com o resultado obtido.
        assertEquals("REPROVADO", resultado);
    }

    @Test
    void deveCalcularMediaComParteDecimal() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(5, 6.5);

        assertEquals(5.75, resultado, 0.0001);
    }

    @Test
    void deveRetornarZeroParaArrayDeMediasVazio() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {});

        assertEquals(0, resultado);
    }

    @Test
    void deveContarUmAprovadoEmArrayComUmElemento() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {7});

        assertEquals(1, resultado);
    }

    @Test
    void deveContarAprovadosEMediasNaoAprovadasEmArrayComVariosElementos() {
        Boletim boletim = new Boletim();

        int resultado = boletim.contarAprovados(new double[] {8, 5, 7, 6});

        assertEquals(2, resultado);

    }
}
