import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IncriptionComponent } from '../list/incription.component';
import { IncriptionDetailComponent } from '../detail/incription-detail.component';
import { IncriptionUpdateComponent } from '../update/incription-update.component';
import { IncriptionRoutingResolveService } from './incription-routing-resolve.service';

const incriptionRoute: Routes = [
  {
    path: '',
    component: IncriptionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IncriptionDetailComponent,
    resolve: {
      incription: IncriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IncriptionUpdateComponent,
    resolve: {
      incription: IncriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IncriptionUpdateComponent,
    resolve: {
      incription: IncriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(incriptionRoute)],
  exports: [RouterModule],
})
export class IncriptionRoutingModule {}
