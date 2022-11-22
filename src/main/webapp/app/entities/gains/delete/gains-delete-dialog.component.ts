import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGains } from '../gains.model';
import { GainsService } from '../service/gains.service';

@Component({
  templateUrl: './gains-delete-dialog.component.html',
})
export class GainsDeleteDialogComponent {
  gains?: IGains;

  constructor(protected gainsService: GainsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gainsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
