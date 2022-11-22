export interface IChoix {
  id?: number;
}

export class Choix implements IChoix {
  constructor(public id?: number) {}
}

export function getChoixIdentifier(choix: IChoix): number | undefined {
  return choix.id;
}
