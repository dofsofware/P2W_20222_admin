import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IncriptionComponent } from './list/incription.component';
import { IncriptionDetailComponent } from './detail/incription-detail.component';
import { IncriptionUpdateComponent } from './update/incription-update.component';
import { IncriptionDeleteDialogComponent } from './delete/incription-delete-dialog.component';
import { IncriptionRoutingModule } from './route/incription-routing.module';

@NgModule({
  imports: [SharedModule, IncriptionRoutingModule],
  declarations: [IncriptionComponent, IncriptionDetailComponent, IncriptionUpdateComponent, IncriptionDeleteDialogComponent],
  entryComponents: [IncriptionDeleteDialogComponent],
})
export class IncriptionModule {}
