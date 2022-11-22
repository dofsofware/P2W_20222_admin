import dayjs from 'dayjs/esm';
import { IGains } from 'app/entities/gains/gains.model';
import { Niveau } from 'app/entities/enumerations/niveau.model';

export interface IAbonne {
  id?: number;
  identifiant?: string;
  telephone?: string;
  motDePasse?: string;
  score?: number;
  niveau?: Niveau;
  createdAt?: dayjs.Dayjs;
  dernierePaticipation?: dayjs.Dayjs | null;
  actif?: boolean;
  codeRactivation?: string | null;
  gains?: IGains[] | null;
}

export class Abonne implements IAbonne {
  constructor(
    public id?: number,
    public identifiant?: string,
    public telephone?: string,
    public motDePasse?: string,
    public score?: number,
    public niveau?: Niveau,
    public createdAt?: dayjs.Dayjs,
    public dernierePaticipation?: dayjs.Dayjs | null,
    public actif?: boolean,
    public codeRactivation?: string | null,
    public gains?: IGains[] | null
  ) {
    this.actif = this.actif ?? false;
  }
}

export function getAbonneIdentifier(abonne: IAbonne): number | undefined {
  return abonne.id;
}
