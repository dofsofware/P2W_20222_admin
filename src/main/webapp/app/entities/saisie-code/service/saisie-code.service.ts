import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISaisieCode, getSaisieCodeIdentifier } from '../saisie-code.model';

export type EntityResponseType = HttpResponse<ISaisieCode>;
export type EntityArrayResponseType = HttpResponse<ISaisieCode[]>;

@Injectable({ providedIn: 'root' })
export class SaisieCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/saisie-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(saisieCode: ISaisieCode): Observable<EntityResponseType> {
    return this.http.post<ISaisieCode>(this.resourceUrl, saisieCode, { observe: 'response' });
  }

  update(saisieCode: ISaisieCode): Observable<EntityResponseType> {
    return this.http.put<ISaisieCode>(`${this.resourceUrl}/${getSaisieCodeIdentifier(saisieCode) as number}`, saisieCode, {
      observe: 'response',
    });
  }

  partialUpdate(saisieCode: ISaisieCode): Observable<EntityResponseType> {
    return this.http.patch<ISaisieCode>(`${this.resourceUrl}/${getSaisieCodeIdentifier(saisieCode) as number}`, saisieCode, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISaisieCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISaisieCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSaisieCodeToCollectionIfMissing(
    saisieCodeCollection: ISaisieCode[],
    ...saisieCodesToCheck: (ISaisieCode | null | undefined)[]
  ): ISaisieCode[] {
    const saisieCodes: ISaisieCode[] = saisieCodesToCheck.filter(isPresent);
    if (saisieCodes.length > 0) {
      const saisieCodeCollectionIdentifiers = saisieCodeCollection.map(saisieCodeItem => getSaisieCodeIdentifier(saisieCodeItem)!);
      const saisieCodesToAdd = saisieCodes.filter(saisieCodeItem => {
        const saisieCodeIdentifier = getSaisieCodeIdentifier(saisieCodeItem);
        if (saisieCodeIdentifier == null || saisieCodeCollectionIdentifiers.includes(saisieCodeIdentifier)) {
          return false;
        }
        saisieCodeCollectionIdentifiers.push(saisieCodeIdentifier);
        return true;
      });
      return [...saisieCodesToAdd, ...saisieCodeCollection];
    }
    return saisieCodeCollection;
  }
}
