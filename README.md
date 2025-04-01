# Sistema de Controle de Ponto

Este projeto foi gerado com JHipster. Abaixo estão as instruções para executar e personalizar o projeto.

## Pré-requisitos

- Java 11+
- Node.js 12+
- PostgreSQL (pode ser executado com Docker)

## Executando o projeto

1. Inicie o banco de dados:

   ```bash
   docker start postgres-timesheet
   ```

2. Inicie o backend:

   ```bash
   ./mvnw
   ```

3. Em outro terminal, inicie o frontend:

   ```bash
   npm start
   ```

4. Acesse o sistema em `http://localhost:9000`

## Finalizando configuração

Os componentes foram criados automaticamente, mas você ainda precisa adicionar manualmente as rotas no arquivo:

1. Abra o arquivo `src/main/webapp/app/routes.tsx` e adicione:

   ```jsx
   import TimesheetDashboard from 'app/modules/dashboard/timesheet-dashboard';
   ```

   E nas rotas:

   ```jsx
   <ErrorBoundaryRoute path="/dashboard" component={TimesheetDashboard} />
   ```

   E nas rotas de admin:

   ```jsx
   <ErrorBoundaryRoute path="/admin/timesheet" component={TimesheetAdminPanel} />
   ```

2. Adicione os itens de menu no arquivo `src/main/webapp/app/shared/layout/header/header-components.tsx`:

   ```jsx
   <MenuItem icon="tachometer-alt" to="/dashboard">
     <Translate contentKey="global.menu.dashboard">Dashboard</Translate>
   </MenuItem>
   ```

   E no menu de administração:

   ```jsx
   <MenuItem icon="clipboard-list" to="/admin/timesheet">
     <Translate contentKey="global.menu.admin.timesheet">Controle de Ponto</Translate>
   </MenuItem>
   ```

## Recursos do Sistema

- Registro de ponto com múltiplas entradas
- Controle de horas extras
- Dashboard com visualização de dados
- Painel administrativo
- Ajuda de custo para trabalho presencial
- Relatórios consolidados
