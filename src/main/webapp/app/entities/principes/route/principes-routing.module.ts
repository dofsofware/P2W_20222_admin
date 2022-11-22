import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrincipesComponent } from '../list/principes.component';
import { PrincipesDetailComponent } from '../detail/principes-detail.component';
import { PrincipesUpdateComponent } from '../update/principes-update.component';
import { PrincipesRoutingResolveService } from './principes-routing-resolve.service';

const principesRoute: Routes = [
  {
    path: '',
    component: PrincipesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrincipesDetailComponent,
    resolve: {
      principes: PrincipesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrincipesUpdateComponent,
    resolve: {
      principes: PrincipesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrincipesUpdateComponent,
    resolve: {
      principes: PrincipesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(principesRoute)],
  exports: [RouterModule],
})
export class PrincipesRoutingModule {}
