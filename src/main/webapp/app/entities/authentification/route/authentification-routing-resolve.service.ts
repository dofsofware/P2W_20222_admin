import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAuthentification, Authentification } from '../authentification.model';
import { AuthentificationService } from '../service/authentification.service';

@Injectable({ providedIn: 'root' })
export class AuthentificationRoutingResolveService implements Resolve<IAuthentification> {
  constructor(protected service: AuthentificationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAuthentification> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((authentification: HttpResponse<Authentification>) => {
          if (authentification.body) {
            return of(authentification.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Authentification());
  }
}
