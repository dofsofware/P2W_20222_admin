import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SaisieCodeComponent } from '../list/saisie-code.component';
import { SaisieCodeDetailComponent } from '../detail/saisie-code-detail.component';
import { SaisieCodeUpdateComponent } from '../update/saisie-code-update.component';
import { SaisieCodeRoutingResolveService } from './saisie-code-routing-resolve.service';

const saisieCodeRoute: Routes = [
  {
    path: '',
    component: SaisieCodeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SaisieCodeDetailComponent,
    resolve: {
      saisieCode: SaisieCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SaisieCodeUpdateComponent,
    resolve: {
      saisieCode: SaisieCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SaisieCodeUpdateComponent,
    resolve: {
      saisieCode: SaisieCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(saisieCodeRoute)],
  exports: [RouterModule],
})
export class SaisieCodeRoutingModule {}
