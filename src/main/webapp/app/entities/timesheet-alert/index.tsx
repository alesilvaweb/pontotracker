import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TimesheetAlert from './timesheet-alert';
import TimesheetAlertDetail from './timesheet-alert-detail';
import TimesheetAlertUpdate from './timesheet-alert-update';
import TimesheetAlertDeleteDialog from './timesheet-alert-delete-dialog';

const TimesheetAlertRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TimesheetAlert />} />
    <Route path="new" element={<TimesheetAlertUpdate />} />
    <Route path=":id">
      <Route index element={<TimesheetAlertDetail />} />
      <Route path="edit" element={<TimesheetAlertUpdate />} />
      <Route path="delete" element={<TimesheetAlertDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimesheetAlertRoutes;
