import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAbonne, getAbonneIdentifier } from '../abonne.model';

export type EntityResponseType = HttpResponse<IAbonne>;
export type EntityArrayResponseType = HttpResponse<IAbonne[]>;

@Injectable({ providedIn: 'root' })
export class AbonneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/abonnes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(abonne: IAbonne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abonne);
    return this.http
      .post<IAbonne>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(abonne: IAbonne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abonne);
    return this.http
      .put<IAbonne>(`${this.resourceUrl}/${getAbonneIdentifier(abonne) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(abonne: IAbonne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abonne);
    return this.http
      .patch<IAbonne>(`${this.resourceUrl}/${getAbonneIdentifier(abonne) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAbonne>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAbonne[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAbonneToCollectionIfMissing(abonneCollection: IAbonne[], ...abonnesToCheck: (IAbonne | null | undefined)[]): IAbonne[] {
    const abonnes: IAbonne[] = abonnesToCheck.filter(isPresent);
    if (abonnes.length > 0) {
      const abonneCollectionIdentifiers = abonneCollection.map(abonneItem => getAbonneIdentifier(abonneItem)!);
      const abonnesToAdd = abonnes.filter(abonneItem => {
        const abonneIdentifier = getAbonneIdentifier(abonneItem);
        if (abonneIdentifier == null || abonneCollectionIdentifiers.includes(abonneIdentifier)) {
          return false;
        }
        abonneCollectionIdentifiers.push(abonneIdentifier);
        return true;
      });
      return [...abonnesToAdd, ...abonneCollection];
    }
    return abonneCollection;
  }

  protected convertDateFromClient(abonne: IAbonne): IAbonne {
    return Object.assign({}, abonne, {
      createdAt: abonne.createdAt?.isValid() ? abonne.createdAt.toJSON() : undefined,
      dernierePaticipation: abonne.dernierePaticipation?.isValid() ? abonne.dernierePaticipation.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.dernierePaticipation = res.body.dernierePaticipation ? dayjs(res.body.dernierePaticipation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((abonne: IAbonne) => {
        abonne.createdAt = abonne.createdAt ? dayjs(abonne.createdAt) : undefined;
        abonne.dernierePaticipation = abonne.dernierePaticipation ? dayjs(abonne.dernierePaticipation) : undefined;
      });
    }
    return res;
  }
}
