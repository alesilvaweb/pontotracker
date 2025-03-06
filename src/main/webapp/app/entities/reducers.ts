import systemConfig from 'app/entities/system-config/system-config.reducer';
import company from 'app/entities/company/company.reducer';
import companyAllowance from 'app/entities/company-allowance/company-allowance.reducer';
import timesheet from 'app/entities/timesheet/timesheet.reducer';
import timeEntry from 'app/entities/time-entry/time-entry.reducer';
import timesheetReport from 'app/entities/timesheet-report/timesheet-report.reducer';
import timesheetAlert from 'app/entities/timesheet-alert/timesheet-alert.reducer';
import userPreference from 'app/entities/user-preference/user-preference.reducer';
import timesheetAudit from 'app/entities/timesheet-audit/timesheet-audit.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  systemConfig,
  company,
  companyAllowance,
  timesheet,
  timeEntry,
  timesheetReport,
  timesheetAlert,
  userPreference,
  timesheetAudit,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
