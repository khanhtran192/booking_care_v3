import { IDoctor } from 'app/shared/model/doctor.model';
import { IHospital } from 'app/shared/model/hospital.model';

export interface IDepartment {
  id?: number;
  departmentName?: string;
  description?: string | null;
  doctors?: IDoctor[] | null;
  hospital?: IHospital | null;
}

export const defaultValue: Readonly<IDepartment> = {};
