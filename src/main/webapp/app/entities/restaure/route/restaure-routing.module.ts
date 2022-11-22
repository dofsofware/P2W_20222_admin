import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RestaureComponent } from '../list/restaure.component';
import { RestaureDetailComponent } from '../detail/restaure-detail.component';
import { RestaureUpdateComponent } from '../update/restaure-update.component';
import { RestaureRoutingResolveService } from './restaure-routing-resolve.service';

const restaureRoute: Routes = [
  {
    path: '',
    component: RestaureComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RestaureDetailComponent,
    resolve: {
      restaure: RestaureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RestaureUpdateComponent,
    resolve: {
      restaure: RestaureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RestaureUpdateComponent,
    resolve: {
      restaure: RestaureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(restaureRoute)],
  exports: [RouterModule],
})
export class RestaureRoutingModule {}
