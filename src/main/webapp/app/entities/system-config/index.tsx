import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SystemConfig from './system-config';
import SystemConfigDetail from './system-config-detail';
import SystemConfigUpdate from './system-config-update';
import SystemConfigDeleteDialog from './system-config-delete-dialog';

const SystemConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SystemConfig />} />
    <Route path="new" element={<SystemConfigUpdate />} />
    <Route path=":id">
      <Route index element={<SystemConfigDetail />} />
      <Route path="edit" element={<SystemConfigUpdate />} />
      <Route path="delete" element={<SystemConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SystemConfigRoutes;
