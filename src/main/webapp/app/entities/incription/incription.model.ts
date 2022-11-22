export interface IIncription {
  id?: number;
}

export class Incription implements IIncription {
  constructor(public id?: number) {}
}

export function getIncriptionIdentifier(incription: IIncription): number | undefined {
  return incription.id;
}
