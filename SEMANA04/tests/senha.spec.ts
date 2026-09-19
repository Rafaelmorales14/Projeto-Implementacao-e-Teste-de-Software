import { test, expect } from '@playwright/test';

test.describe('cadastro de senha', () => {
  test('cadastra senha válida no limite mínimo', async ({ page }) => {
    await page.goto('/senha');
    await page.getByLabel('Nova senha').fill('Abcdefg1');
    await page.getByLabel('Confirmar senha').fill('Abcdefg1');
    await page.getByRole('button', { name: 'Cadastrar senha' }).click();

    await expect(page.getByRole('status')).toHaveText('Senha cadastrada');
    await expect(page.getByLabel('Nova senha')).toHaveValue('');
    await expect(page.getByLabel('Confirmar senha')).toHaveValue('');
  });

  test('cadastra senha válida no limite máximo', async ({ page }) => {
    const senha = 'Abcdefghijklmnopqr1';
    await page.goto('/senha');
    await page.getByLabel('Nova senha').fill(senha);
    await page.getByLabel('Confirmar senha').fill(senha);
    await page.getByRole('button', { name: 'Cadastrar senha' }).click();

    await expect(page.getByRole('status')).toHaveText('Senha cadastrada');
  });

  for (const caso of [
    { nome: 'com menos de 8 caracteres', senha: 'Abcdef1' },
    { nome: 'com mais de 20 caracteres', senha: 'Abcdefghijklmnopqrstu1' },
    { nome: 'sem letra maiúscula', senha: 'abcdefg1' },
    { nome: 'sem letra minúscula', senha: 'ABCDEFG1' },
    { nome: 'sem número', senha: 'Abcdefgh' },
    { nome: 'com espaço', senha: 'Abc defg1' },
    { nome: 'vazia', senha: '' },
  ]) {
    test(`rejeita senha ${caso.nome}`, async ({ page }) => {
      await page.goto('/senha');
      await page.getByLabel('Nova senha').fill(caso.senha);
      await page.getByLabel('Confirmar senha').fill(caso.senha);
      await page.getByRole('button', { name: 'Cadastrar senha' }).click();

      await expect(page.getByRole('alert')).toHaveText('Senha fora do padrão');
    });
  }

  test('rejeita confirmação diferente', async ({ page }) => {
    await page.goto('/senha');
    await page.getByLabel('Nova senha').fill('Abcdefg1');
    await page.getByLabel('Confirmar senha').fill('Abcdefg2');
    await page.getByRole('button', { name: 'Cadastrar senha' }).click();

    await expect(page.getByRole('alert')).toHaveText('As senhas não coincidem');
  });
});