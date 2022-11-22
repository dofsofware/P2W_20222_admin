import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GainsComponent } from '../list/gains.component';
import { GainsDetailComponent } from '../detail/gains-detail.component';
import { GainsUpdateComponent } from '../update/gains-update.component';
import { GainsRoutingResolveService } from './gains-routing-resolve.service';

const gainsRoute: Routes = [
  {
    path: '',
    component: GainsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GainsDetailComponent,
    resolve: {
      gains: GainsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GainsUpdateComponent,
    resolve: {
      gains: GainsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GainsUpdateComponent,
    resolve: {
      gains: GainsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gainsRoute)],
  exports: [RouterModule],
})
export class GainsRoutingModule {}
