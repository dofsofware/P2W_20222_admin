import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGains, Gains } from '../gains.model';
import { GainsService } from '../service/gains.service';

@Injectable({ providedIn: 'root' })
export class GainsRoutingResolveService implements Resolve<IGains> {
  constructor(protected service: GainsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGains> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gains: HttpResponse<Gains>) => {
          if (gains.body) {
            return of(gains.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Gains());
  }
}
