import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ICompany } from 'app/shared/model/company.model';
import { WorkModality } from 'app/shared/model/enumerations/work-modality.model';

export interface ITimesheet {
  id?: number;
  date?: dayjs.Dayjs;
  modality?: keyof typeof WorkModality;
  classification?: string | null;
  description?: string | null;
  totalHours?: number;
  overtimeHours?: number | null;
  allowanceValue?: number | null;
  status?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  approvedAt?: dayjs.Dayjs | null;
  approvedBy?: string | null;
  user?: IUser;
  company?: ICompany;
}

export const defaultValue: Readonly<ITimesheet> = {};
