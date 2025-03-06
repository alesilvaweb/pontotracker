import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompanyAllowance from './company-allowance';
import CompanyAllowanceDetail from './company-allowance-detail';
import CompanyAllowanceUpdate from './company-allowance-update';
import CompanyAllowanceDeleteDialog from './company-allowance-delete-dialog';

const CompanyAllowanceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompanyAllowance />} />
    <Route path="new" element={<CompanyAllowanceUpdate />} />
    <Route path=":id">
      <Route index element={<CompanyAllowanceDetail />} />
      <Route path="edit" element={<CompanyAllowanceUpdate />} />
      <Route path="delete" element={<CompanyAllowanceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompanyAllowanceRoutes;
