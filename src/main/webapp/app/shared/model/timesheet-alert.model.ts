import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ITimesheetAlert {
  id?: number;
  userId?: number;
  timesheetId?: number;
  date?: dayjs.Dayjs;
  type?: string;
  message?: string;
  severity?: string;
  status?: string;
  createdAt?: dayjs.Dayjs;
  resolvedAt?: dayjs.Dayjs | null;
  resolution?: string | null;
  resolvedBy?: IUser | null;
}

export const defaultValue: Readonly<ITimesheetAlert> = {};
