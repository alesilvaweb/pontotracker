import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ITimesheetReport {
  id?: number;
  userId?: number;
  userName?: string;
  companyId?: number;
  companyName?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  totalRegularHours?: number;
  totalOvertimeHours?: number;
  totalAllowance?: number;
  status?: string;
  generatedAt?: dayjs.Dayjs;
  approvedAt?: dayjs.Dayjs | null;
  comments?: string | null;
  generatedBy?: IUser;
  approvedBy?: IUser | null;
}

export const defaultValue: Readonly<ITimesheetReport> = {};
