import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MotDePasseSettingComponent } from './list/mot-de-passe-setting.component';
import { MotDePasseSettingDetailComponent } from './detail/mot-de-passe-setting-detail.component';
import { MotDePasseSettingUpdateComponent } from './update/mot-de-passe-setting-update.component';
import { MotDePasseSettingDeleteDialogComponent } from './delete/mot-de-passe-setting-delete-dialog.component';
import { MotDePasseSettingRoutingModule } from './route/mot-de-passe-setting-routing.module';

@NgModule({
  imports: [SharedModule, MotDePasseSettingRoutingModule],
  declarations: [
    MotDePasseSettingComponent,
    MotDePasseSettingDetailComponent,
    MotDePasseSettingUpdateComponent,
    MotDePasseSettingDeleteDialogComponent,
  ],
  entryComponents: [MotDePasseSettingDeleteDialogComponent],
})
export class MotDePasseSettingModule {}
