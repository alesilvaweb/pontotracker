import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SystemConfig from './system-config';
import Company from './company';
import CompanyAllowance from './company-allowance';
import Timesheet from './timesheet';
import TimeEntry from './time-entry';
import TimesheetReport from './timesheet-report';
import TimesheetAlert from './timesheet-alert';
import UserPreference from './user-preference';
import TimesheetAudit from './timesheet-audit';
import TimesheetDashboard from 'app/modules/dashboard/timesheet-dashboard';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="system-config/*" element={<SystemConfig />} />
        <Route path="company/*" element={<Company />} />
        <Route path="company-allowance/*" element={<CompanyAllowance />} />
        <Route path="timesheet/*" element={<Timesheet />} />
        <Route path="time-entry/*" element={<TimeEntry />} />
        <Route path="timesheet-report/*" element={<TimesheetReport />} />
        <Route path="timesheet-alert/*" element={<TimesheetAlert />} />
        <Route path="user-preference/*" element={<UserPreference />} />
        <Route path="timesheet-audit/*" element={<TimesheetAudit />} />
        <Route path="dashboard" element={<TimesheetDashboard />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
