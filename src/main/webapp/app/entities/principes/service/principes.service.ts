import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrincipes, getPrincipesIdentifier } from '../principes.model';

export type EntityResponseType = HttpResponse<IPrincipes>;
export type EntityArrayResponseType = HttpResponse<IPrincipes[]>;

@Injectable({ providedIn: 'root' })
export class PrincipesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/principes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(principes: IPrincipes): Observable<EntityResponseType> {
    return this.http.post<IPrincipes>(this.resourceUrl, principes, { observe: 'response' });
  }

  update(principes: IPrincipes): Observable<EntityResponseType> {
    return this.http.put<IPrincipes>(`${this.resourceUrl}/${getPrincipesIdentifier(principes) as number}`, principes, {
      observe: 'response',
    });
  }

  partialUpdate(principes: IPrincipes): Observable<EntityResponseType> {
    return this.http.patch<IPrincipes>(`${this.resourceUrl}/${getPrincipesIdentifier(principes) as number}`, principes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrincipes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrincipes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrincipesToCollectionIfMissing(
    principesCollection: IPrincipes[],
    ...principesToCheck: (IPrincipes | null | undefined)[]
  ): IPrincipes[] {
    const principes: IPrincipes[] = principesToCheck.filter(isPresent);
    if (principes.length > 0) {
      const principesCollectionIdentifiers = principesCollection.map(principesItem => getPrincipesIdentifier(principesItem)!);
      const principesToAdd = principes.filter(principesItem => {
        const principesIdentifier = getPrincipesIdentifier(principesItem);
        if (principesIdentifier == null || principesCollectionIdentifiers.includes(principesIdentifier)) {
          return false;
        }
        principesCollectionIdentifiers.push(principesIdentifier);
        return true;
      });
      return [...principesToAdd, ...principesCollection];
    }
    return principesCollection;
  }
}
