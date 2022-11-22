import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInfosAbonne, getInfosAbonneIdentifier } from '../infos-abonne.model';

export type EntityResponseType = HttpResponse<IInfosAbonne>;
export type EntityArrayResponseType = HttpResponse<IInfosAbonne[]>;

@Injectable({ providedIn: 'root' })
export class InfosAbonneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/infos-abonnes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(infosAbonne: IInfosAbonne): Observable<EntityResponseType> {
    return this.http.post<IInfosAbonne>(this.resourceUrl, infosAbonne, { observe: 'response' });
  }

  update(infosAbonne: IInfosAbonne): Observable<EntityResponseType> {
    return this.http.put<IInfosAbonne>(`${this.resourceUrl}/${getInfosAbonneIdentifier(infosAbonne) as number}`, infosAbonne, {
      observe: 'response',
    });
  }

  partialUpdate(infosAbonne: IInfosAbonne): Observable<EntityResponseType> {
    return this.http.patch<IInfosAbonne>(`${this.resourceUrl}/${getInfosAbonneIdentifier(infosAbonne) as number}`, infosAbonne, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInfosAbonne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInfosAbonne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInfosAbonneToCollectionIfMissing(
    infosAbonneCollection: IInfosAbonne[],
    ...infosAbonnesToCheck: (IInfosAbonne | null | undefined)[]
  ): IInfosAbonne[] {
    const infosAbonnes: IInfosAbonne[] = infosAbonnesToCheck.filter(isPresent);
    if (infosAbonnes.length > 0) {
      const infosAbonneCollectionIdentifiers = infosAbonneCollection.map(infosAbonneItem => getInfosAbonneIdentifier(infosAbonneItem)!);
      const infosAbonnesToAdd = infosAbonnes.filter(infosAbonneItem => {
        const infosAbonneIdentifier = getInfosAbonneIdentifier(infosAbonneItem);
        if (infosAbonneIdentifier == null || infosAbonneCollectionIdentifiers.includes(infosAbonneIdentifier)) {
          return false;
        }
        infosAbonneCollectionIdentifiers.push(infosAbonneIdentifier);
        return true;
      });
      return [...infosAbonnesToAdd, ...infosAbonneCollection];
    }
    return infosAbonneCollection;
  }
}
