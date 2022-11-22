import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMotDePasseSetting } from '../mot-de-passe-setting.model';
import { MotDePasseSettingService } from '../service/mot-de-passe-setting.service';

@Component({
  templateUrl: './mot-de-passe-setting-delete-dialog.component.html',
})
export class MotDePasseSettingDeleteDialogComponent {
  motDePasseSetting?: IMotDePasseSetting;

  constructor(protected motDePasseSettingService: MotDePasseSettingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.motDePasseSettingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
