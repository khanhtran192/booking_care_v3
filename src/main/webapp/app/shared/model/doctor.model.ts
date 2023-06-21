import dayjs from 'dayjs';
import { ITimeSlot } from 'app/shared/model/time-slot.model';
import { IOrder } from 'app/shared/model/order.model';
import { IDepartment } from 'app/shared/model/department.model';

export interface IDoctor {
  id?: number;
  name?: string;
  email?: string | null;
  phoneNumber?: string | null;
  dateOfBirth?: string | null;
  hospitalId?: number | null;
  degree?: string | null;
  rate?: number | null;
  specialize?: string | null;
  timeSlots?: ITimeSlot[] | null;
  orders?: IOrder[] | null;
  department?: IDepartment | null;
}

export const defaultValue: Readonly<IDoctor> = {};
