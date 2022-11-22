import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRestaure, Restaure } from '../restaure.model';
import { RestaureService } from '../service/restaure.service';

@Injectable({ providedIn: 'root' })
export class RestaureRoutingResolveService implements Resolve<IRestaure> {
  constructor(protected service: RestaureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestaure> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((restaure: HttpResponse<Restaure>) => {
          if (restaure.body) {
            return of(restaure.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Restaure());
  }
}
