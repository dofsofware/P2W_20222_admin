import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISaisieCode, SaisieCode } from '../saisie-code.model';
import { SaisieCodeService } from '../service/saisie-code.service';

@Component({
  selector: 'jhi-saisie-code-update',
  templateUrl: './saisie-code-update.component.html',
})
export class SaisieCodeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected saisieCodeService: SaisieCodeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ saisieCode }) => {
      this.updateForm(saisieCode);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const saisieCode = this.createFromForm();
    if (saisieCode.id !== undefined) {
      this.subscribeToSaveResponse(this.saisieCodeService.update(saisieCode));
    } else {
      this.subscribeToSaveResponse(this.saisieCodeService.create(saisieCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISaisieCode>>): void {
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

  protected updateForm(saisieCode: ISaisieCode): void {
    this.editForm.patchValue({
      id: saisieCode.id,
    });
  }

  protected createFromForm(): ISaisieCode {
    return {
      ...new SaisieCode(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
