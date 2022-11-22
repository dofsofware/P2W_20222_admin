import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResultat, Resultat } from '../resultat.model';
import { ResultatService } from '../service/resultat.service';

@Injectable({ providedIn: 'root' })
export class ResultatRoutingResolveService implements Resolve<IResultat> {
  constructor(protected service: ResultatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResultat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resultat: HttpResponse<Resultat>) => {
          if (resultat.body) {
            return of(resultat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Resultat());
  }
}
