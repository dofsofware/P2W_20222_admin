import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChoix } from '../choix.model';
import { ChoixService } from '../service/choix.service';

@Component({
  templateUrl: './choix-delete-dialog.component.html',
})
export class ChoixDeleteDialogComponent {
  choix?: IChoix;

  constructor(protected choixService: ChoixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.choixService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
