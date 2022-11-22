export interface IResultat {
  id?: number;
}

export class Resultat implements IResultat {
  constructor(public id?: number) {}
}

export function getResultatIdentifier(resultat: IResultat): number | undefined {
  return resultat.id;
}
