import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISaisieCode, SaisieCode } from '../saisie-code.model';
import { SaisieCodeService } from '../service/saisie-code.service';

@Injectable({ providedIn: 'root' })
export class SaisieCodeRoutingResolveService implements Resolve<ISaisieCode> {
  constructor(protected service: SaisieCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISaisieCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((saisieCode: HttpResponse<SaisieCode>) => {
          if (saisieCode.body) {
            return of(saisieCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SaisieCode());
  }
}
