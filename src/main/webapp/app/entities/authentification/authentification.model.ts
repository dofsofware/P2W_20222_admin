export interface IAuthentification {
  id?: number;
}

export class Authentification implements IAuthentification {
  constructor(public id?: number) {}
}

export function getAuthentificationIdentifier(authentification: IAuthentification): number | undefined {
  return authentification.id;
}
