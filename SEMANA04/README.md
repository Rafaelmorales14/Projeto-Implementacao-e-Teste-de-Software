# Playwright no teste funcional

Projeto executável associado à apresentação sobre teste funcional com Playwright.
O código transforma casos de teste baseados em regras, classes de equivalência e
valores-limite em jornadas automatizadas no navegador.

## O que ele demonstra

- login válido e inválido;
- asserções de URL, conteúdo e estado da sessão;
- classes de equivalência e valores-limite para idade;
- classes de equivalência e regras de negócio para cálculo de frete;
- limites e classes inválidas para validação de senha;
- execução em Chromium, Firefox e WebKit;
- servidor iniciado automaticamente pelo Playwright;
- relatório HTML, screenshot em falha e trace na primeira repetição.

## Casos automatizados

| Interface | Cenários cobertos | Oráculo principal |
| --- | --- | --- |
| Login | credenciais válidas e senha inválida | URL, área autenticada, mensagem e sessão |
| Idade | 17, 18, 65, 66, vazio, texto e decimal | mensagem e papel semântico do resultado |
| Frete | CEP iniciado por 8, demais CEPs, R$ 200,00 e entradas inválidas | valor do frete ou mensagem de erro |
| Senha | 8 e 20 caracteres, classes inválidas e confirmação diferente | mensagem, papel semântico e limpeza do formulário |

Os testes usam `getByLabel`, `getByRole` e `getByTestId`, evitando seletores
acoplados ao layout. Cada teste abre uma nova página/contexto e pode ser executado
independentemente.


## Pré-requisitos no Windows

- Node.js LTS instalado: https://nodejs.org/
- PowerShell, Prompt de Comando ou Terminal do Windows

Confira a instalação:

```powershell
node --version
npm --version
```

## Preparar o projeto

No PowerShell, entre na pasta do projeto e instale as dependências:

```powershell
cd "aulas\SEMANA04\exemplo-playwright_win"
npm install
npm run browsers
```

O comando `npm run browsers` instala o Chromium usado pelos testes e só precisa
ser repetido quando o Playwright solicitar uma nova versão.

## Usar o sistema antes dos testes

No primeiro terminal:

```powershell
npm run start
```

Acesse:

- http://127.0.0.1:3000/login
- http://127.0.0.1:3000/idade
- http://127.0.0.1:3000/frete
- http://127.0.0.1:3000/senha

Mantenha esse terminal aberto enquanto usa o sistema. Para encerrá-lo, pressione
`Ctrl+C`.

## Executar os testes

Com o sistema ainda aberto, abra um segundo terminal na mesma pasta e execute:

```powershell
npm test
```

O Playwright reutiliza o servidor que já está rodando. Se ele não estiver aberto,
o próprio Playwright inicia e encerra o servidor automaticamente.

Somente Chromium, mostrando o navegador:

```powershell
npm run test:headed
```

Interface de execução e depuração:

```powershell
npm run test:ui
```

Depurador passo a passo:

```powershell
npm run test:debug
```

Abrir o último relatório:

```powershell
npm run report
```

Para instalar todos os navegadores e executar os projetos:

```powershell
npm run browsers:all
npm run test:all
```



## Atividade implementada

Os casos funcionais estão em:

- `tests/idade.spec.ts`
- `tests/login.spec.ts`
- `tests/frete.spec.ts`
- `tests/senha.spec.ts`

Para a atividade de login, foram usadas as credenciais didáticas:

- e-mail: `ana@exemplo.com`
- senha: `SenhaSegura123!`

> ISSO É UM EXEMPLO: Não reutilize credenciais fixas dessa forma em um sistema real.

## Evidências e depuração

Uma execução aprovada gera o relatório HTML em `playwright-report/`. Em caso de
falha, a configuração salva screenshot e registra trace na primeira repetição.
Esses artefatos ajudam a investigar a execução, mas a aprovação vem das
asserções específicas do caso, não apenas da imagem da página.

Comandos úteis:

```powershell
npm test
npm run test:headed
npm run test:ui
npm run test:debug
npm run report
```

Para validar todos os navegadores:

```powershell
npm run browsers:all
npm run test:all
```

## Precondições

- Node.js e npm instalados;
- dependências instaladas com `npm install`;
- navegador Chromium instalado com `npm run browsers`;
- aplicação local iniciada automaticamente pelo `webServer` do Playwright;
- nenhuma conta externa ou serviço de terceiros é necessário.

## Publicação

O projeto pode ser versionado no GitHub pela conta do aluno. Não há senhas ou
tokens no código; para publicar, use a chave SSH configurada no computador e
confira o remoto antes do `git push`.
