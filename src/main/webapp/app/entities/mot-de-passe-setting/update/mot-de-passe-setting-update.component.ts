import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMotDePasseSetting, MotDePasseSetting } from '../mot-de-passe-setting.model';
import { MotDePasseSettingService } from '../service/mot-de-passe-setting.service';

@Component({
  selector: 'jhi-mot-de-passe-setting-update',
  templateUrl: './mot-de-passe-setting-update.component.html',
})
export class MotDePasseSettingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(
    protected motDePasseSettingService: MotDePasseSettingService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ motDePasseSetting }) => {
      this.updateForm(motDePasseSetting);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const motDePasseSetting = this.createFromForm();
    if (motDePasseSetting.id !== undefined) {
      this.subscribeToSaveResponse(this.motDePasseSettingService.update(motDePasseSetting));
    } else {
      this.subscribeToSaveResponse(this.motDePasseSettingService.create(motDePasseSetting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMotDePasseSetting>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(motDePasseSetting: IMotDePasseSetting): void {
    this.editForm.patchValue({
      id: motDePasseSetting.id,
    });
  }

  protected createFromForm(): IMotDePasseSetting {
    return {
      ...new MotDePasseSetting(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
