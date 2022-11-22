import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestaure, getRestaureIdentifier } from '../restaure.model';

export type EntityResponseType = HttpResponse<IRestaure>;
export type EntityArrayResponseType = HttpResponse<IRestaure[]>;

@Injectable({ providedIn: 'root' })
export class RestaureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaure: IRestaure): Observable<EntityResponseType> {
    return this.http.post<IRestaure>(this.resourceUrl, restaure, { observe: 'response' });
  }

  update(restaure: IRestaure): Observable<EntityResponseType> {
    return this.http.put<IRestaure>(`${this.resourceUrl}/${getRestaureIdentifier(restaure) as number}`, restaure, { observe: 'response' });
  }

  partialUpdate(restaure: IRestaure): Observable<EntityResponseType> {
    return this.http.patch<IRestaure>(`${this.resourceUrl}/${getRestaureIdentifier(restaure) as number}`, restaure, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRestaure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRestaureToCollectionIfMissing(restaureCollection: IRestaure[], ...restauresToCheck: (IRestaure | null | undefined)[]): IRestaure[] {
    const restaures: IRestaure[] = restauresToCheck.filter(isPresent);
    if (restaures.length > 0) {
      const restaureCollectionIdentifiers = restaureCollection.map(restaureItem => getRestaureIdentifier(restaureItem)!);
      const restauresToAdd = restaures.filter(restaureItem => {
        const restaureIdentifier = getRestaureIdentifier(restaureItem);
        if (restaureIdentifier == null || restaureCollectionIdentifiers.includes(restaureIdentifier)) {
          return false;
        }
        restaureCollectionIdentifiers.push(restaureIdentifier);
        return true;
      });
      return [...restauresToAdd, ...restaureCollection];
    }
    return restaureCollection;
  }
}
