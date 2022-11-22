export interface IReponse {
  id?: number;
}

export class Reponse implements IReponse {
  constructor(public id?: number) {}
}

export function getReponseIdentifier(reponse: IReponse): number | undefined {
  return reponse.id;
}
