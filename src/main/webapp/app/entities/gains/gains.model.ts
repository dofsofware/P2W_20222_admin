import dayjs from 'dayjs/esm';
import { IAbonne } from 'app/entities/abonne/abonne.model';

export interface IGains {
  id?: number;
  telephone?: string;
  minute?: number;
  megabit?: number;
  createdAt?: dayjs.Dayjs;
  abonnes?: IAbonne[] | null;
}

export class Gains implements IGains {
  constructor(
    public id?: number,
    public telephone?: string,
    public minute?: number,
    public megabit?: number,
    public createdAt?: dayjs.Dayjs,
    public abonnes?: IAbonne[] | null
  ) {}
}

export function getGainsIdentifier(gains: IGains): number | undefined {
  return gains.id;
}
