import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInfosAbonne, InfosAbonne } from '../infos-abonne.model';
import { InfosAbonneService } from '../service/infos-abonne.service';

@Injectable({ providedIn: 'root' })
export class InfosAbonneRoutingResolveService implements Resolve<IInfosAbonne> {
  constructor(protected service: InfosAbonneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfosAbonne> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((infosAbonne: HttpResponse<InfosAbonne>) => {
          if (infosAbonne.body) {
            return of(infosAbonne.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InfosAbonne());
  }
}
