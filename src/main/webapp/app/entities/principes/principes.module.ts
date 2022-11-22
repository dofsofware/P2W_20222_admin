import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrincipesComponent } from './list/principes.component';
import { PrincipesDetailComponent } from './detail/principes-detail.component';
import { PrincipesUpdateComponent } from './update/principes-update.component';
import { PrincipesDeleteDialogComponent } from './delete/principes-delete-dialog.component';
import { PrincipesRoutingModule } from './route/principes-routing.module';

@NgModule({
  imports: [SharedModule, PrincipesRoutingModule],
  declarations: [PrincipesComponent, PrincipesDetailComponent, PrincipesUpdateComponent, PrincipesDeleteDialogComponent],
  entryComponents: [PrincipesDeleteDialogComponent],
})
export class PrincipesModule {}
