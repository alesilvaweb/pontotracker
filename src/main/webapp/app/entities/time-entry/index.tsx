import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TimeEntry from './time-entry';
import TimeEntryDetail from './time-entry-detail';
import TimeEntryUpdate from './time-entry-update';
import TimeEntryDeleteDialog from './time-entry-delete-dialog';

const TimeEntryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TimeEntry />} />
    <Route path="new" element={<TimeEntryUpdate />} />
    <Route path=":id">
      <Route index element={<TimeEntryDetail />} />
      <Route path="edit" element={<TimeEntryUpdate />} />
      <Route path="delete" element={<TimeEntryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimeEntryRoutes;
