import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMotDePasseSetting, MotDePasseSetting } from '../mot-de-passe-setting.model';
import { MotDePasseSettingService } from '../service/mot-de-passe-setting.service';

@Injectable({ providedIn: 'root' })
export class MotDePasseSettingRoutingResolveService implements Resolve<IMotDePasseSetting> {
  constructor(protected service: MotDePasseSettingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMotDePasseSetting> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((motDePasseSetting: HttpResponse<MotDePasseSetting>) => {
          if (motDePasseSetting.body) {
            return of(motDePasseSetting.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MotDePasseSetting());
  }
}
