import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIncription, getIncriptionIdentifier } from '../incription.model';

export type EntityResponseType = HttpResponse<IIncription>;
export type EntityArrayResponseType = HttpResponse<IIncription[]>;

@Injectable({ providedIn: 'root' })
export class IncriptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/incriptions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(incription: IIncription): Observable<EntityResponseType> {
    return this.http.post<IIncription>(this.resourceUrl, incription, { observe: 'response' });
  }

  update(incription: IIncription): Observable<EntityResponseType> {
    return this.http.put<IIncription>(`${this.resourceUrl}/${getIncriptionIdentifier(incription) as number}`, incription, {
      observe: 'response',
    });
  }

  partialUpdate(incription: IIncription): Observable<EntityResponseType> {
    return this.http.patch<IIncription>(`${this.resourceUrl}/${getIncriptionIdentifier(incription) as number}`, incription, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIncription>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIncription[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIncriptionToCollectionIfMissing(
    incriptionCollection: IIncription[],
    ...incriptionsToCheck: (IIncription | null | undefined)[]
  ): IIncription[] {
    const incriptions: IIncription[] = incriptionsToCheck.filter(isPresent);
    if (incriptions.length > 0) {
      const incriptionCollectionIdentifiers = incriptionCollection.map(incriptionItem => getIncriptionIdentifier(incriptionItem)!);
      const incriptionsToAdd = incriptions.filter(incriptionItem => {
        const incriptionIdentifier = getIncriptionIdentifier(incriptionItem);
        if (incriptionIdentifier == null || incriptionCollectionIdentifiers.includes(incriptionIdentifier)) {
          return false;
        }
        incriptionCollectionIdentifiers.push(incriptionIdentifier);
        return true;
      });
      return [...incriptionsToAdd, ...incriptionCollection];
    }
    return incriptionCollection;
  }
}
