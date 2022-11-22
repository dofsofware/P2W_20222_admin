import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AuthentificationComponent } from './list/authentification.component';
import { AuthentificationDetailComponent } from './detail/authentification-detail.component';
import { AuthentificationUpdateComponent } from './update/authentification-update.component';
import { AuthentificationDeleteDialogComponent } from './delete/authentification-delete-dialog.component';
import { AuthentificationRoutingModule } from './route/authentification-routing.module';

@NgModule({
  imports: [SharedModule, AuthentificationRoutingModule],
  declarations: [
    AuthentificationComponent,
    AuthentificationDetailComponent,
    AuthentificationUpdateComponent,
    AuthentificationDeleteDialogComponent,
  ],
  entryComponents: [AuthentificationDeleteDialogComponent],
})
export class AuthentificationModule {}
