import { IMarketplace } from 'app/shared/model/marketplace.model';

export interface IProduct {
  id?: number;
  name?: string;
  price?: number;
  amount?: number;
  marketplace?: IMarketplace | null;
}

export const defaultValue: Readonly<IProduct> = {};
