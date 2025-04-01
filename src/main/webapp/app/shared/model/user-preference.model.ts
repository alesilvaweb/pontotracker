import { IUser } from 'app/shared/model/user.model';

export interface IUserPreference {
  id?: number;
  defaultCompanyId?: number;
  emailNotifications?: boolean;
  smsNotifications?: boolean;
  pushNotifications?: boolean;
  reportFrequency?: string | null;
  weekStartDay?: number | null;
  user?: IUser;
}

export const defaultValue: Readonly<IUserPreference> = {
  emailNotifications: false,
  smsNotifications: false,
  pushNotifications: false,
};
