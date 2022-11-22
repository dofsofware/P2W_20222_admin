import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIncription, Incription } from '../incription.model';
import { IncriptionService } from '../service/incription.service';

@Injectable({ providedIn: 'root' })
export class IncriptionRoutingResolveService implements Resolve<IIncription> {
  constructor(protected service: IncriptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIncription> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((incription: HttpResponse<Incription>) => {
          if (incription.body) {
            return of(incription.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Incription());
  }
}
