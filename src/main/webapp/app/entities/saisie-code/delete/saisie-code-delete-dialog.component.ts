import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISaisieCode } from '../saisie-code.model';
import { SaisieCodeService } from '../service/saisie-code.service';

@Component({
  templateUrl: './saisie-code-delete-dialog.component.html',
})
export class SaisieCodeDeleteDialogComponent {
  saisieCode?: ISaisieCode;

  constructor(protected saisieCodeService: SaisieCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.saisieCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
