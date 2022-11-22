import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SaisieCodeComponent } from './list/saisie-code.component';
import { SaisieCodeDetailComponent } from './detail/saisie-code-detail.component';
import { SaisieCodeUpdateComponent } from './update/saisie-code-update.component';
import { SaisieCodeDeleteDialogComponent } from './delete/saisie-code-delete-dialog.component';
import { SaisieCodeRoutingModule } from './route/saisie-code-routing.module';

@NgModule({
  imports: [SharedModule, SaisieCodeRoutingModule],
  declarations: [SaisieCodeComponent, SaisieCodeDetailComponent, SaisieCodeUpdateComponent, SaisieCodeDeleteDialogComponent],
  entryComponents: [SaisieCodeDeleteDialogComponent],
})
export class SaisieCodeModule {}
