export interface IPlay {
  id?: number;
}

export class Play implements IPlay {
  constructor(public id?: number) {}
}

export function getPlayIdentifier(play: IPlay): number | undefined {
  return play.id;
}
