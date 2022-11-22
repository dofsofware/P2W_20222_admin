import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrincipes, Principes } from '../principes.model';
import { PrincipesService } from '../service/principes.service';

@Injectable({ providedIn: 'root' })
export class PrincipesRoutingResolveService implements Resolve<IPrincipes> {
  constructor(protected service: PrincipesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrincipes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((principes: HttpResponse<Principes>) => {
          if (principes.body) {
            return of(principes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Principes());
  }
}
