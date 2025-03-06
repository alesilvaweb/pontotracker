import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TimesheetReport from './timesheet-report';
import TimesheetReportDetail from './timesheet-report-detail';
import TimesheetReportUpdate from './timesheet-report-update';
import TimesheetReportDeleteDialog from './timesheet-report-delete-dialog';

const TimesheetReportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TimesheetReport />} />
    <Route path="new" element={<TimesheetReportUpdate />} />
    <Route path=":id">
      <Route index element={<TimesheetReportDetail />} />
      <Route path="edit" element={<TimesheetReportUpdate />} />
      <Route path="delete" element={<TimesheetReportDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimesheetReportRoutes;
