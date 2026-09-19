# Sistema de Reserva de Laboratórios

Documentação de testes da disciplina **Implementação e Teste de Software**.

Este repositório reúne o **plano de teste** e os **casos de teste** elaborados para o Sistema de Reserva de Laboratórios. O foco é garantir que uma reserva só seja aceita quando a sala estiver disponível, for compatível com a turma, tiver capacidade suficiente e não estiver em manutenção — além de validar horário de funcionamento, permissões, cancelamento, notificações, desempenho, auditoria e acesso por unidade.

---

## O que foi feito

Foram produzidos dois artefatos de teste, alinhados aos requisitos funcionais e não funcionais do sistema:

| Documento | Conteúdo |
| --- | --- |
| [Plano de Teste](./Plano%20de%20Teste.docx) | Escopo, estratégia, riscos, ambiente, critérios de entrada/saída e priorização |
| [Caso de Teste](./Caso%20de%20Teste.docx) | Casos de teste detalhados (CT-RES-001 a CT-RES-011), com pré-condições, passos e resultados esperados |

O plano cobre testes de **unidade**, **integração** e **sistema**, usando técnicas como análise de valor limite, particionamento de equivalência, tabela de decisão, transição de estado e caminho negativo.

---

## Requisitos cobertos

**Funcionais**

- **RF-01** — Reservar sala disponível e compatível com a turma
- **RF-02** — Impedir sobreposição de horário na mesma sala
- **RF-03** — Impedir reserva quando a turma excede a capacidade
- **RF-04** — Bloquear reserva em sala em manutenção
- **RF-05** — Permitir reservas apenas entre 07h30 e 22h30
- **RF-06** — Somente a coordenação altera reserva de outro professor
- **RF-07** — Cancelamento libera o horário e registra histórico
- **RF-08** — Alteração ou cancelamento gera notificação

**Não funcionais**

- **RNF-01** — Busca de salas e horários responde em até 2 segundos
- **RNF-02** — Operações possuem trilha de auditoria
- **RNF-03** — Acesso limitado às unidades autorizadas do usuário

---

## Autores

| Nome | RA |
| --- | --- |
| Rafael Blasques Morales | 24124872-2 |
| Lucas Domingues de Souza | 24538741-2 |
