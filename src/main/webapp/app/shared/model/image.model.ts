import { ImageType } from 'app/shared/model/enumerations/image-type.model';

export interface IImage {
  id?: number;
  name?: string | null;
  path?: string | null;
  type?: ImageType | null;
  hospitalId?: number | null;
  doctorId?: number | null;
  departmentId?: number | null;
}

export const defaultValue: Readonly<IImage> = {};
