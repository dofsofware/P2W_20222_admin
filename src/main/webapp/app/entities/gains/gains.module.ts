import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GainsComponent } from './list/gains.component';
import { GainsDetailComponent } from './detail/gains-detail.component';
import { GainsUpdateComponent } from './update/gains-update.component';
import { GainsDeleteDialogComponent } from './delete/gains-delete-dialog.component';
import { GainsRoutingModule } from './route/gains-routing.module';

@NgModule({
  imports: [SharedModule, GainsRoutingModule],
  declarations: [GainsComponent, GainsDetailComponent, GainsUpdateComponent, GainsDeleteDialogComponent],
  entryComponents: [GainsDeleteDialogComponent],
})
export class GainsModule {}
