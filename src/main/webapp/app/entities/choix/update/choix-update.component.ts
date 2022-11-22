import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IChoix, Choix } from '../choix.model';
import { ChoixService } from '../service/choix.service';

@Component({
  selector: 'jhi-choix-update',
  templateUrl: './choix-update.component.html',
})
export class ChoixUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected choixService: ChoixService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ choix }) => {
      this.updateForm(choix);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const choix = this.createFromForm();
    if (choix.id !== undefined) {
      this.subscribeToSaveResponse(this.choixService.update(choix));
    } else {
      this.subscribeToSaveResponse(this.choixService.create(choix));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChoix>>): void {
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

  protected updateForm(choix: IChoix): void {
    this.editForm.patchValue({
      id: choix.id,
    });
  }

  protected createFromForm(): IChoix {
    return {
      ...new Choix(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
