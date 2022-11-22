export interface IProfil {
  id?: number;
}

export class Profil implements IProfil {
  constructor(public id?: number) {}
}

export function getProfilIdentifier(profil: IProfil): number | undefined {
  return profil.id;
}
