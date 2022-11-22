import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncription } from '../incription.model';
import { IncriptionService } from '../service/incription.service';

@Component({
  templateUrl: './incription-delete-dialog.component.html',
})
export class IncriptionDeleteDialogComponent {
  incription?: IIncription;

  constructor(protected incriptionService: IncriptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incriptionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
