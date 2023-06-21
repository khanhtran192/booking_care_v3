import { IDepartment } from 'app/shared/model/department.model';
import { IPack } from 'app/shared/model/pack.model';
import { FacilityType } from 'app/shared/model/enumerations/facility-type.model';

export interface IHospital {
  id?: number;
  name?: string;
  address?: string;
  email?: string | null;
  phoneNumber?: string | null;
  description?: string | null;
  workDay?: string | null;
  workTime?: string | null;
  type?: FacilityType | null;
  procedure?: string | null;
  departments?: IDepartment[] | null;
  packs?: IPack[] | null;
}

export const defaultValue: Readonly<IHospital> = {};
