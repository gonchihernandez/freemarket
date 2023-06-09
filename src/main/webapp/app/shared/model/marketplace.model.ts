import { IProduct } from 'app/shared/model/product.model';

export interface IMarketplace {
  id?: number;
  name?: string;
  products?: IProduct[] | null;
}

export const defaultValue: Readonly<IMarketplace> = {};
