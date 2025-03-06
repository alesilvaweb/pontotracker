import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/system-config">
        <Translate contentKey="global.menu.entities.systemConfig" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/company-allowance">
        <Translate contentKey="global.menu.entities.companyAllowance" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/timesheet">
        <Translate contentKey="global.menu.entities.timesheet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/time-entry">
        <Translate contentKey="global.menu.entities.timeEntry" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/timesheet-report">
        <Translate contentKey="global.menu.entities.timesheetReport" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/timesheet-alert">
        <Translate contentKey="global.menu.entities.timesheetAlert" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-preference">
        <Translate contentKey="global.menu.entities.userPreference" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/timesheet-audit">
        <Translate contentKey="global.menu.entities.timesheetAudit" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
