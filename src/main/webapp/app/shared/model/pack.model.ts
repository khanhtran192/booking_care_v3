import { ITimeSlot } from 'app/shared/model/time-slot.model';
import { IOrder } from 'app/shared/model/order.model';
import { IHospital } from 'app/shared/model/hospital.model';

export interface IPack {
  id?: number;
  nane?: string | null;
  description?: string | null;
  timeSlots?: ITimeSlot[] | null;
  orders?: IOrder[] | null;
  hospital?: IHospital | null;
}

export const defaultValue: Readonly<IPack> = {};
