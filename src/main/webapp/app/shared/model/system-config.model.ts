import dayjs from 'dayjs';

export interface ISystemConfig {
  id?: number;
  dailyWorkHours?: number;
  weeklyWorkHours?: number;
  overtimeNormalRate?: number;
  overtimeSpecialRate?: number;
  lastUpdated?: dayjs.Dayjs;
}

export const defaultValue: Readonly<ISystemConfig> = {};
