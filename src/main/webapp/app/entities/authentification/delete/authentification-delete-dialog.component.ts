import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAuthentification } from '../authentification.model';
import { AuthentificationService } from '../service/authentification.service';

@Component({
  templateUrl: './authentification-delete-dialog.component.html',
})
export class AuthentificationDeleteDialogComponent {
  authentification?: IAuthentification;

  constructor(protected authentificationService: AuthentificationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.authentificationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
