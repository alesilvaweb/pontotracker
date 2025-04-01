import dayjs from 'dayjs';

export interface ICompany {
  id?: number;
  name?: string;
  cnpj?: string;
  active?: boolean;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
}

export const defaultValue: Readonly<ICompany> = {
  active: false,
};
