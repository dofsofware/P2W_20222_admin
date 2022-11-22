import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RestaureComponent } from './list/restaure.component';
import { RestaureDetailComponent } from './detail/restaure-detail.component';
import { RestaureUpdateComponent } from './update/restaure-update.component';
import { RestaureDeleteDialogComponent } from './delete/restaure-delete-dialog.component';
import { RestaureRoutingModule } from './route/restaure-routing.module';

@NgModule({
  imports: [SharedModule, RestaureRoutingModule],
  declarations: [RestaureComponent, RestaureDetailComponent, RestaureUpdateComponent, RestaureDeleteDialogComponent],
  entryComponents: [RestaureDeleteDialogComponent],
})
export class RestaureModule {}
