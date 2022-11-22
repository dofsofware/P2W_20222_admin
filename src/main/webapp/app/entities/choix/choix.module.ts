import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChoixComponent } from './list/choix.component';
import { ChoixDetailComponent } from './detail/choix-detail.component';
import { ChoixUpdateComponent } from './update/choix-update.component';
import { ChoixDeleteDialogComponent } from './delete/choix-delete-dialog.component';
import { ChoixRoutingModule } from './route/choix-routing.module';

@NgModule({
  imports: [SharedModule, ChoixRoutingModule],
  declarations: [ChoixComponent, ChoixDetailComponent, ChoixUpdateComponent, ChoixDeleteDialogComponent],
  entryComponents: [ChoixDeleteDialogComponent],
})
export class ChoixModule {}
