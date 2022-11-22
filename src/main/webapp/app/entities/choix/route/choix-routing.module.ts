import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChoixComponent } from '../list/choix.component';
import { ChoixDetailComponent } from '../detail/choix-detail.component';
import { ChoixUpdateComponent } from '../update/choix-update.component';
import { ChoixRoutingResolveService } from './choix-routing-resolve.service';

const choixRoute: Routes = [
  {
    path: '',
    component: ChoixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChoixDetailComponent,
    resolve: {
      choix: ChoixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChoixUpdateComponent,
    resolve: {
      choix: ChoixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChoixUpdateComponent,
    resolve: {
      choix: ChoixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(choixRoute)],
  exports: [RouterModule],
})
export class ChoixRoutingModule {}
