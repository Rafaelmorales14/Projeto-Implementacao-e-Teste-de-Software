import { test, expect } from '@playwright/test';

test.describe('calculadora de frete', () => {
  test('calcula frete reduzido para CEP iniciado por 8', async ({ page }) => {
    await page.goto('/frete');
    await page.getByLabel('CEP').fill('80000000');
    await page.getByLabel('Valor do pedido').fill('100,00');
    await page.getByRole('button', { name: 'Calcular frete' }).click();

    await expect(page.getByRole('status')).toHaveText('Frete: R$ 15,00');
  });

  test('calcula frete padrão para os demais CEPs', async ({ page }) => {
    await page.goto('/frete');
    await page.getByLabel('CEP').fill('01000000');
    await page.getByLabel('Valor do pedido').fill('100,00');
    await page.getByRole('button', { name: 'Calcular frete' }).click();

    await expect(page.getByRole('status')).toHaveText('Frete: R$ 25,00');
  });

  test('oferece frete grátis no limite de R$ 200,00', async ({ page }) => {
    await page.goto('/frete');
    await page.getByLabel('CEP').fill('01000000');
    await page.getByLabel('Valor do pedido').fill('200,00');
    await page.getByRole('button', { name: 'Calcular frete' }).click();

    await expect(page.getByRole('status')).toHaveText('Frete grátis');
  });

  for (const caso of [
    { nome: 'CEP com menos de 8 dígitos', cep: '8000000', valor: '100,00' },
    { nome: 'CEP com letras', cep: '80A00000', valor: '100,00' },
    { nome: 'valor zero', cep: '80000000', valor: '0,00' },
    { nome: 'valor vazio', cep: '80000000', valor: '' },
  ]) {
    test(`rejeita ${caso.nome}`, async ({ page }) => {
      await page.goto('/frete');
      await page.getByLabel('CEP').fill(caso.cep);
      await page.getByLabel('Valor do pedido').fill(caso.valor);
      await page.getByRole('button', { name: 'Calcular frete' }).click();

      await expect(page.getByRole('alert')).toHaveText('Dados inválidos');
    });
  }
});