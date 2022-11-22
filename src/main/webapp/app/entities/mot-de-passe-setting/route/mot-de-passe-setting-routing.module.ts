import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MotDePasseSettingComponent } from '../list/mot-de-passe-setting.component';
import { MotDePasseSettingDetailComponent } from '../detail/mot-de-passe-setting-detail.component';
import { MotDePasseSettingUpdateComponent } from '../update/mot-de-passe-setting-update.component';
import { MotDePasseSettingRoutingResolveService } from './mot-de-passe-setting-routing-resolve.service';

const motDePasseSettingRoute: Routes = [
  {
    path: '',
    component: MotDePasseSettingComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MotDePasseSettingDetailComponent,
    resolve: {
      motDePasseSetting: MotDePasseSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MotDePasseSettingUpdateComponent,
    resolve: {
      motDePasseSetting: MotDePasseSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MotDePasseSettingUpdateComponent,
    resolve: {
      motDePasseSetting: MotDePasseSettingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(motDePasseSettingRoute)],
  exports: [RouterModule],
})
export class MotDePasseSettingRoutingModule {}
