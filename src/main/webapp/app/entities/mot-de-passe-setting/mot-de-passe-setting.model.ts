export interface IMotDePasseSetting {
  id?: number;
}

export class MotDePasseSetting implements IMotDePasseSetting {
  constructor(public id?: number) {}
}

export function getMotDePasseSettingIdentifier(motDePasseSetting: IMotDePasseSetting): number | undefined {
  return motDePasseSetting.id;
}
