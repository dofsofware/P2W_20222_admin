export interface IInfosAbonne {
  id?: number;
}

export class InfosAbonne implements IInfosAbonne {
  constructor(public id?: number) {}
}

export function getInfosAbonneIdentifier(infosAbonne: IInfosAbonne): number | undefined {
  return infosAbonne.id;
}
