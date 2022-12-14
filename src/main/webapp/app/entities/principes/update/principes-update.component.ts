import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPrincipes, Principes } from '../principes.model';
import { PrincipesService } from '../service/principes.service';

@Component({
  selector: 'jhi-principes-update',
  templateUrl: './principes-update.component.html',
})
export class PrincipesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected principesService: PrincipesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ principes }) => {
      this.updateForm(principes);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const principes = this.createFromForm();
    if (principes.id !== undefined) {
      this.subscribeToSaveResponse(this.principesService.update(principes));
    } else {
      this.subscribeToSaveResponse(this.principesService.create(principes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrincipes>>): void {
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

  protected updateForm(principes: IPrincipes): void {
    this.editForm.patchValue({
      id: principes.id,
    });
  }

  protected createFromForm(): IPrincipes {
    return {
      ...new Principes(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
