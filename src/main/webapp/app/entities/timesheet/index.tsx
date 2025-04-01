import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Timesheet from './timesheet';
import TimesheetDetail from './timesheet-detail';
import TimesheetUpdate from './timesheet-update';
import TimesheetDeleteDialog from './timesheet-delete-dialog';

const TimesheetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Timesheet />} />
    <Route path="new" element={<TimesheetUpdate />} />
    <Route path=":id">
      <Route index element={<TimesheetDetail />} />
      <Route path="edit" element={<TimesheetUpdate />} />
      <Route path="delete" element={<TimesheetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimesheetRoutes;
