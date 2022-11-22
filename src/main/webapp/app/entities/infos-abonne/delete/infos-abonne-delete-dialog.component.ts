import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfosAbonne } from '../infos-abonne.model';
import { InfosAbonneService } from '../service/infos-abonne.service';

@Component({
  templateUrl: './infos-abonne-delete-dialog.component.html',
})
export class InfosAbonneDeleteDialogComponent {
  infosAbonne?: IInfosAbonne;

  constructor(protected infosAbonneService: InfosAbonneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infosAbonneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
