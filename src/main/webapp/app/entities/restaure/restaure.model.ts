export interface IRestaure {
  id?: number;
}

export class Restaure implements IRestaure {
  constructor(public id?: number) {}
}

export function getRestaureIdentifier(restaure: IRestaure): number | undefined {
  return restaure.id;
}
