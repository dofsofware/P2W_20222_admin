import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlay, getPlayIdentifier } from '../play.model';

export type EntityResponseType = HttpResponse<IPlay>;
export type EntityArrayResponseType = HttpResponse<IPlay[]>;

@Injectable({ providedIn: 'root' })
export class PlayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(play: IPlay): Observable<EntityResponseType> {
    return this.http.post<IPlay>(this.resourceUrl, play, { observe: 'response' });
  }

  update(play: IPlay): Observable<EntityResponseType> {
    return this.http.put<IPlay>(`${this.resourceUrl}/${getPlayIdentifier(play) as number}`, play, { observe: 'response' });
  }

  partialUpdate(play: IPlay): Observable<EntityResponseType> {
    return this.http.patch<IPlay>(`${this.resourceUrl}/${getPlayIdentifier(play) as number}`, play, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlay>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlay[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlayToCollectionIfMissing(playCollection: IPlay[], ...playsToCheck: (IPlay | null | undefined)[]): IPlay[] {
    const plays: IPlay[] = playsToCheck.filter(isPresent);
    if (plays.length > 0) {
      const playCollectionIdentifiers = playCollection.map(playItem => getPlayIdentifier(playItem)!);
      const playsToAdd = plays.filter(playItem => {
        const playIdentifier = getPlayIdentifier(playItem);
        if (playIdentifier == null || playCollectionIdentifiers.includes(playIdentifier)) {
          return false;
        }
        playCollectionIdentifiers.push(playIdentifier);
        return true;
      });
      return [...playsToAdd, ...playCollection];
    }
    return playCollection;
  }
}
