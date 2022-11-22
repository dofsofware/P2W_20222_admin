import dayjs from 'dayjs/esm';
import { ChoixDuGain } from 'app/entities/enumerations/choix-du-gain.model';

export interface IRecette {
  id?: number;
  telephone?: string;
  createdAt?: dayjs.Dayjs;
  montant?: number;
  choixDuGain?: ChoixDuGain;
}

export class Recette implements IRecette {
  constructor(
    public id?: number,
    public telephone?: string,
    public createdAt?: dayjs.Dayjs,
    public montant?: number,
    public choixDuGain?: ChoixDuGain
  ) {}
}

export function getRecetteIdentifier(recette: IRecette): number | undefined {
  return recette.id;
}
