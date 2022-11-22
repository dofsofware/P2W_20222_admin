import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AuthentificationComponent } from '../list/authentification.component';
import { AuthentificationDetailComponent } from '../detail/authentification-detail.component';
import { AuthentificationUpdateComponent } from '../update/authentification-update.component';
import { AuthentificationRoutingResolveService } from './authentification-routing-resolve.service';

const authentificationRoute: Routes = [
  {
    path: '',
    component: AuthentificationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AuthentificationDetailComponent,
    resolve: {
      authentification: AuthentificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AuthentificationUpdateComponent,
    resolve: {
      authentification: AuthentificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AuthentificationUpdateComponent,
    resolve: {
      authentification: AuthentificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(authentificationRoute)],
  exports: [RouterModule],
})
export class AuthentificationRoutingModule {}
