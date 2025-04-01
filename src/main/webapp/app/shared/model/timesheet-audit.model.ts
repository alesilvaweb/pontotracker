import dayjs from 'dayjs';

export interface ITimesheetAudit {
  id?: number;
  entityType?: string;
  entityId?: number;
  action?: string;
  timestamp?: dayjs.Dayjs;
  userId?: number;
  oldValues?: string | null;
  newValues?: string | null;
}

export const defaultValue: Readonly<ITimesheetAudit> = {};
