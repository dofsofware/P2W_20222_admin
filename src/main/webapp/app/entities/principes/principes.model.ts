export interface IPrincipes {
  id?: number;
}

export class Principes implements IPrincipes {
  constructor(public id?: number) {}
}

export function getPrincipesIdentifier(principes: IPrincipes): number | undefined {
  return principes.id;
}
