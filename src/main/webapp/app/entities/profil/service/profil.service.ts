import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProfil, getProfilIdentifier } from '../profil.model';

export type EntityResponseType = HttpResponse<IProfil>;
export type EntityArrayResponseType = HttpResponse<IProfil[]>;

@Injectable({ providedIn: 'root' })
export class ProfilService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/profils');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(profil: IProfil): Observable<EntityResponseType> {
    return this.http.post<IProfil>(this.resourceUrl, profil, { observe: 'response' });
  }

  update(profil: IProfil): Observable<EntityResponseType> {
    return this.http.put<IProfil>(`${this.resourceUrl}/${getProfilIdentifier(profil) as number}`, profil, { observe: 'response' });
  }

  partialUpdate(profil: IProfil): Observable<EntityResponseType> {
    return this.http.patch<IProfil>(`${this.resourceUrl}/${getProfilIdentifier(profil) as number}`, profil, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfil>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfil[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProfilToCollectionIfMissing(profilCollection: IProfil[], ...profilsToCheck: (IProfil | null | undefined)[]): IProfil[] {
    const profils: IProfil[] = profilsToCheck.filter(isPresent);
    if (profils.length > 0) {
      const profilCollectionIdentifiers = profilCollection.map(profilItem => getProfilIdentifier(profilItem)!);
      const profilsToAdd = profils.filter(profilItem => {
        const profilIdentifier = getProfilIdentifier(profilItem);
        if (profilIdentifier == null || profilCollectionIdentifiers.includes(profilIdentifier)) {
          return false;
        }
        profilCollectionIdentifiers.push(profilIdentifier);
        return true;
      });
      return [...profilsToAdd, ...profilCollection];
    }
    return profilCollection;
  }
}
