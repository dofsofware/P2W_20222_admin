import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAuthentification, Authentification } from '../authentification.model';
import { AuthentificationService } from '../service/authentification.service';

@Component({
  selector: 'jhi-authentification-update',
  templateUrl: './authentification-update.component.html',
})
export class AuthentificationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(
    protected authentificationService: AuthentificationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authentification }) => {
      this.updateForm(authentification);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const authentification = this.createFromForm();
    if (authentification.id !== undefined) {
      this.subscribeToSaveResponse(this.authentificationService.update(authentification));
    } else {
      this.subscribeToSaveResponse(this.authentificationService.create(authentification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuthentification>>): void {
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

  protected updateForm(authentification: IAuthentification): void {
    this.editForm.patchValue({
      id: authentification.id,
    });
  }

  protected createFromForm(): IAuthentification {
    return {
      ...new Authentification(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
