import { IDoctor } from 'app/shared/model/doctor.model';
import { IPack } from 'app/shared/model/pack.model';

export interface ITimeSlot {
  id?: number;
  time?: string;
  description?: string | null;
  price?: number;
  status?: boolean | null;
  doctor?: IDoctor | null;
  pack?: IPack | null;
}

export const defaultValue: Readonly<ITimeSlot> = {
  status: false,
};
