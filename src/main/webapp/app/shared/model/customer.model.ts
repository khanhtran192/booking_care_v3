import dayjs from 'dayjs';
import { IOrder } from 'app/shared/model/order.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface ICustomer {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  fullName?: string | null;
  address?: string | null;
  dateOfBirth?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  idCard?: string | null;
  gender?: Gender | null;
  orders?: IOrder[] | null;
}

export const defaultValue: Readonly<ICustomer> = {};
