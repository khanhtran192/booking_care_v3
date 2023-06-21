import { IOrder } from 'app/shared/model/order.model';

export interface IDiagnose {
  id?: number;
  description?: string | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IDiagnose> = {};
