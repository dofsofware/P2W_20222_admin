import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMotDePasseSetting, getMotDePasseSettingIdentifier } from '../mot-de-passe-setting.model';

export type EntityResponseType = HttpResponse<IMotDePasseSetting>;
export type EntityArrayResponseType = HttpResponse<IMotDePasseSetting[]>;

@Injectable({ providedIn: 'root' })
export class MotDePasseSettingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mot-de-passe-settings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(motDePasseSetting: IMotDePasseSetting): Observable<EntityResponseType> {
    return this.http.post<IMotDePasseSetting>(this.resourceUrl, motDePasseSetting, { observe: 'response' });
  }

  update(motDePasseSetting: IMotDePasseSetting): Observable<EntityResponseType> {
    return this.http.put<IMotDePasseSetting>(
      `${this.resourceUrl}/${getMotDePasseSettingIdentifier(motDePasseSetting) as number}`,
      motDePasseSetting,
      { observe: 'response' }
    );
  }

  partialUpdate(motDePasseSetting: IMotDePasseSetting): Observable<EntityResponseType> {
    return this.http.patch<IMotDePasseSetting>(
      `${this.resourceUrl}/${getMotDePasseSettingIdentifier(motDePasseSetting) as number}`,
      motDePasseSetting,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMotDePasseSetting>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMotDePasseSetting[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMotDePasseSettingToCollectionIfMissing(
    motDePasseSettingCollection: IMotDePasseSetting[],
    ...motDePasseSettingsToCheck: (IMotDePasseSetting | null | undefined)[]
  ): IMotDePasseSetting[] {
    const motDePasseSettings: IMotDePasseSetting[] = motDePasseSettingsToCheck.filter(isPresent);
    if (motDePasseSettings.length > 0) {
      const motDePasseSettingCollectionIdentifiers = motDePasseSettingCollection.map(
        motDePasseSettingItem => getMotDePasseSettingIdentifier(motDePasseSettingItem)!
      );
      const motDePasseSettingsToAdd = motDePasseSettings.filter(motDePasseSettingItem => {
        const motDePasseSettingIdentifier = getMotDePasseSettingIdentifier(motDePasseSettingItem);
        if (motDePasseSettingIdentifier == null || motDePasseSettingCollectionIdentifiers.includes(motDePasseSettingIdentifier)) {
          return false;
        }
        motDePasseSettingCollectionIdentifiers.push(motDePasseSettingIdentifier);
        return true;
      });
      return [...motDePasseSettingsToAdd, ...motDePasseSettingCollection];
    }
    return motDePasseSettingCollection;
  }
}
