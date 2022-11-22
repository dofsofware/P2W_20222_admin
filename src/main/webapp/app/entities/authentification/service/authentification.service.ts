import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuthentification, getAuthentificationIdentifier } from '../authentification.model';

export type EntityResponseType = HttpResponse<IAuthentification>;
export type EntityArrayResponseType = HttpResponse<IAuthentification[]>;

@Injectable({ providedIn: 'root' })
export class AuthentificationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/authentifications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(authentification: IAuthentification): Observable<EntityResponseType> {
    return this.http.post<IAuthentification>(this.resourceUrl, authentification, { observe: 'response' });
  }

  update(authentification: IAuthentification): Observable<EntityResponseType> {
    return this.http.put<IAuthentification>(
      `${this.resourceUrl}/${getAuthentificationIdentifier(authentification) as number}`,
      authentification,
      { observe: 'response' }
    );
  }

  partialUpdate(authentification: IAuthentification): Observable<EntityResponseType> {
    return this.http.patch<IAuthentification>(
      `${this.resourceUrl}/${getAuthentificationIdentifier(authentification) as number}`,
      authentification,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAuthentification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAuthentification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAuthentificationToCollectionIfMissing(
    authentificationCollection: IAuthentification[],
    ...authentificationsToCheck: (IAuthentification | null | undefined)[]
  ): IAuthentification[] {
    const authentifications: IAuthentification[] = authentificationsToCheck.filter(isPresent);
    if (authentifications.length > 0) {
      const authentificationCollectionIdentifiers = authentificationCollection.map(
        authentificationItem => getAuthentificationIdentifier(authentificationItem)!
      );
      const authentificationsToAdd = authentifications.filter(authentificationItem => {
        const authentificationIdentifier = getAuthentificationIdentifier(authentificationItem);
        if (authentificationIdentifier == null || authentificationCollectionIdentifiers.includes(authentificationIdentifier)) {
          return false;
        }
        authentificationCollectionIdentifiers.push(authentificationIdentifier);
        return true;
      });
      return [...authentificationsToAdd, ...authentificationCollection];
    }
    return authentificationCollection;
  }
}
