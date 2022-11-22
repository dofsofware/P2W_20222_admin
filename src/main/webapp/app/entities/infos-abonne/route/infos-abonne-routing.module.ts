import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InfosAbonneComponent } from '../list/infos-abonne.component';
import { InfosAbonneDetailComponent } from '../detail/infos-abonne-detail.component';
import { InfosAbonneUpdateComponent } from '../update/infos-abonne-update.component';
import { InfosAbonneRoutingResolveService } from './infos-abonne-routing-resolve.service';

const infosAbonneRoute: Routes = [
  {
    path: '',
    component: InfosAbonneComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfosAbonneDetailComponent,
    resolve: {
      infosAbonne: InfosAbonneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfosAbonneUpdateComponent,
    resolve: {
      infosAbonne: InfosAbonneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfosAbonneUpdateComponent,
    resolve: {
      infosAbonne: InfosAbonneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(infosAbonneRoute)],
  exports: [RouterModule],
})
export class InfosAbonneRoutingModule {}
