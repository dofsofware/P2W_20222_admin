import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResultat, getResultatIdentifier } from '../resultat.model';

export type EntityResponseType = HttpResponse<IResultat>;
export type EntityArrayResponseType = HttpResponse<IResultat[]>;

@Injectable({ providedIn: 'root' })
export class ResultatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resultats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resultat: IResultat): Observable<EntityResponseType> {
    return this.http.post<IResultat>(this.resourceUrl, resultat, { observe: 'response' });
  }

  update(resultat: IResultat): Observable<EntityResponseType> {
    return this.http.put<IResultat>(`${this.resourceUrl}/${getResultatIdentifier(resultat) as number}`, resultat, { observe: 'response' });
  }

  partialUpdate(resultat: IResultat): Observable<EntityResponseType> {
    return this.http.patch<IResultat>(`${this.resourceUrl}/${getResultatIdentifier(resultat) as number}`, resultat, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResultat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResultat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addResultatToCollectionIfMissing(resultatCollection: IResultat[], ...resultatsToCheck: (IResultat | null | undefined)[]): IResultat[] {
    const resultats: IResultat[] = resultatsToCheck.filter(isPresent);
    if (resultats.length > 0) {
      const resultatCollectionIdentifiers = resultatCollection.map(resultatItem => getResultatIdentifier(resultatItem)!);
      const resultatsToAdd = resultats.filter(resultatItem => {
        const resultatIdentifier = getResultatIdentifier(resultatItem);
        if (resultatIdentifier == null || resultatCollectionIdentifiers.includes(resultatIdentifier)) {
          return false;
        }
        resultatCollectionIdentifiers.push(resultatIdentifier);
        return true;
      });
      return [...resultatsToAdd, ...resultatCollection];
    }
    return resultatCollection;
  }
}
