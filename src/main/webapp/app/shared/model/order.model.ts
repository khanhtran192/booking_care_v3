import dayjs from 'dayjs';
import { ITimeSlot } from 'app/shared/model/time-slot.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IDoctor } from 'app/shared/model/doctor.model';
import { IPack } from 'app/shared/model/pack.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IOrder {
  id?: number;
  address?: string | null;
  symptom?: string | null;
  date?: string | null;
  status?: OrderStatus | null;
  price?: number | null;
  timeslot?: ITimeSlot | null;
  customer?: ICustomer | null;
  doctor?: IDoctor | null;
  pack?: IPack | null;
}

export const defaultValue: Readonly<IOrder> = {};
