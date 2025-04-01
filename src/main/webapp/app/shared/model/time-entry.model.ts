import dayjs from 'dayjs';
import { ITimesheet } from 'app/shared/model/timesheet.model';
import { EntryType } from 'app/shared/model/enumerations/entry-type.model';
import { OvertimeCategory } from 'app/shared/model/enumerations/overtime-category.model';

export interface ITimeEntry {
  id?: number;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs;
  type?: keyof typeof EntryType;
  overtimeCategory?: keyof typeof OvertimeCategory | null;
  description?: string | null;
  hoursWorked?: number;
  timesheet?: ITimesheet;
}

export const defaultValue: Readonly<ITimeEntry> = {};
