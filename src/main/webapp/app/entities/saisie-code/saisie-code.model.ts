export interface ISaisieCode {
  id?: number;
}

export class SaisieCode implements ISaisieCode {
  constructor(public id?: number) {}
}

export function getSaisieCodeIdentifier(saisieCode: ISaisieCode): number | undefined {
  return saisieCode.id;
}
