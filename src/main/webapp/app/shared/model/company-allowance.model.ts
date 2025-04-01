import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';

export interface ICompanyAllowance {
  id?: number;
  presentialAllowanceValue?: number;
  remoteAllowanceValue?: number;
  fullTimeThresholdHours?: number;
  partTimeAllowancePercentage?: number;
  considerWorkDistance?: boolean;
  minimumDistanceToReceive?: number | null;
  active?: boolean;
  lastUpdated?: dayjs.Dayjs;
  company?: ICompany;
}

export const defaultValue: Readonly<ICompanyAllowance> = {
  considerWorkDistance: false,
  active: false,
};
