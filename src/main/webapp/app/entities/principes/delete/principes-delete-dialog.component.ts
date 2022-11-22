import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrincipes } from '../principes.model';
import { PrincipesService } from '../service/principes.service';

@Component({
  templateUrl: './principes-delete-dialog.component.html',
})
export class PrincipesDeleteDialogComponent {
  principes?: IPrincipes;

  constructor(protected principesService: PrincipesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.principesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
