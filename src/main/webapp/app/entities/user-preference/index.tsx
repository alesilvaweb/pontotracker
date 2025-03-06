import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserPreference from './user-preference';
import UserPreferenceDetail from './user-preference-detail';
import UserPreferenceUpdate from './user-preference-update';
import UserPreferenceDeleteDialog from './user-preference-delete-dialog';

const UserPreferenceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserPreference />} />
    <Route path="new" element={<UserPreferenceUpdate />} />
    <Route path=":id">
      <Route index element={<UserPreferenceDetail />} />
      <Route path="edit" element={<UserPreferenceUpdate />} />
      <Route path="delete" element={<UserPreferenceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserPreferenceRoutes;
