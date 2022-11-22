import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResultat } from '../resultat.model';
import { ResultatService } from '../service/resultat.service';

@Component({
  templateUrl: './resultat-delete-dialog.component.html',
})
export class ResultatDeleteDialogComponent {
  resultat?: IResultat;

  constructor(protected resultatService: ResultatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resultatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
