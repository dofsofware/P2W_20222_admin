import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChoix, Choix } from '../choix.model';
import { ChoixService } from '../service/choix.service';

@Injectable({ providedIn: 'root' })
export class ChoixRoutingResolveService implements Resolve<IChoix> {
  constructor(protected service: ChoixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChoix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((choix: HttpResponse<Choix>) => {
          if (choix.body) {
            return of(choix.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Choix());
  }
}
