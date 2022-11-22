import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaure } from '../restaure.model';
import { RestaureService } from '../service/restaure.service';

@Component({
  templateUrl: './restaure-delete-dialog.component.html',
})
export class RestaureDeleteDialogComponent {
  restaure?: IRestaure;

  constructor(protected restaureService: RestaureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.restaureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
