import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';

@Component({
  templateUrl: './profil-delete-dialog.component.html',
})
export class ProfilDeleteDialogComponent {
  profil?: IProfil;

  constructor(protected profilService: ProfilService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.profilService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
