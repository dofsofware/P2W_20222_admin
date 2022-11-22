import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InfosAbonneComponent } from './list/infos-abonne.component';
import { InfosAbonneDetailComponent } from './detail/infos-abonne-detail.component';
import { InfosAbonneUpdateComponent } from './update/infos-abonne-update.component';
import { InfosAbonneDeleteDialogComponent } from './delete/infos-abonne-delete-dialog.component';
import { InfosAbonneRoutingModule } from './route/infos-abonne-routing.module';

@NgModule({
  imports: [SharedModule, InfosAbonneRoutingModule],
  declarations: [InfosAbonneComponent, InfosAbonneDetailComponent, InfosAbonneUpdateComponent, InfosAbonneDeleteDialogComponent],
  entryComponents: [InfosAbonneDeleteDialogComponent],
})
export class InfosAbonneModule {}
