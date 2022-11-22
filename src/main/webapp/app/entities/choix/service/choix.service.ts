import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChoix, getChoixIdentifier } from '../choix.model';

export type EntityResponseType = HttpResponse<IChoix>;
export type EntityArrayResponseType = HttpResponse<IChoix[]>;

@Injectable({ providedIn: 'root' })
export class ChoixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/choixes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(choix: IChoix): Observable<EntityResponseType> {
    return this.http.post<IChoix>(this.resourceUrl, choix, { observe: 'response' });
  }

  update(choix: IChoix): Observable<EntityResponseType> {
    return this.http.put<IChoix>(`${this.resourceUrl}/${getChoixIdentifier(choix) as number}`, choix, { observe: 'response' });
  }

  partialUpdate(choix: IChoix): Observable<EntityResponseType> {
    return this.http.patch<IChoix>(`${this.resourceUrl}/${getChoixIdentifier(choix) as number}`, choix, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChoix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChoix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChoixToCollectionIfMissing(choixCollection: IChoix[], ...choixesToCheck: (IChoix | null | undefined)[]): IChoix[] {
    const choixes: IChoix[] = choixesToCheck.filter(isPresent);
    if (choixes.length > 0) {
      const choixCollectionIdentifiers = choixCollection.map(choixItem => getChoixIdentifier(choixItem)!);
      const choixesToAdd = choixes.filter(choixItem => {
        const choixIdentifier = getChoixIdentifier(choixItem);
        if (choixIdentifier == null || choixCollectionIdentifiers.includes(choixIdentifier)) {
          return false;
        }
        choixCollectionIdentifiers.push(choixIdentifier);
        return true;
      });
      return [...choixesToAdd, ...choixCollection];
    }
    return choixCollection;
  }
}
