import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGains, getGainsIdentifier } from '../gains.model';

export type EntityResponseType = HttpResponse<IGains>;
export type EntityArrayResponseType = HttpResponse<IGains[]>;

@Injectable({ providedIn: 'root' })
export class GainsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gains');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gains: IGains): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gains);
    return this.http
      .post<IGains>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(gains: IGains): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gains);
    return this.http
      .put<IGains>(`${this.resourceUrl}/${getGainsIdentifier(gains) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(gains: IGains): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gains);
    return this.http
      .patch<IGains>(`${this.resourceUrl}/${getGainsIdentifier(gains) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGains>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGains[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGainsToCollectionIfMissing(gainsCollection: IGains[], ...gainsToCheck: (IGains | null | undefined)[]): IGains[] {
    const gains: IGains[] = gainsToCheck.filter(isPresent);
    if (gains.length > 0) {
      const gainsCollectionIdentifiers = gainsCollection.map(gainsItem => getGainsIdentifier(gainsItem)!);
      const gainsToAdd = gains.filter(gainsItem => {
        const gainsIdentifier = getGainsIdentifier(gainsItem);
        if (gainsIdentifier == null || gainsCollectionIdentifiers.includes(gainsIdentifier)) {
          return false;
        }
        gainsCollectionIdentifiers.push(gainsIdentifier);
        return true;
      });
      return [...gainsToAdd, ...gainsCollection];
    }
    return gainsCollection;
  }

  protected convertDateFromClient(gains: IGains): IGains {
    return Object.assign({}, gains, {
      createdAt: gains.createdAt?.isValid() ? gains.createdAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((gains: IGains) => {
        gains.createdAt = gains.createdAt ? dayjs(gains.createdAt) : undefined;
      });
    }
    return res;
  }
}
