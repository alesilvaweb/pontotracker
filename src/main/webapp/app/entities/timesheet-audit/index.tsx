import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TimesheetAudit from './timesheet-audit';
import TimesheetAuditDetail from './timesheet-audit-detail';
import TimesheetAuditUpdate from './timesheet-audit-update';
import TimesheetAuditDeleteDialog from './timesheet-audit-delete-dialog';

const TimesheetAuditRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TimesheetAudit />} />
    <Route path="new" element={<TimesheetAuditUpdate />} />
    <Route path=":id">
      <Route index element={<TimesheetAuditDetail />} />
      <Route path="edit" element={<TimesheetAuditUpdate />} />
      <Route path="delete" element={<TimesheetAuditDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimesheetAuditRoutes;
