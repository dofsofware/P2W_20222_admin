import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIncription, Incription } from '../incription.model';
import { IncriptionService } from '../service/incription.service';

@Component({
  selector: 'jhi-incription-update',
  templateUrl: './incription-update.component.html',
})
export class IncriptionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected incriptionService: IncriptionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incription }) => {
      this.updateForm(incription);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const incription = this.createFromForm();
    if (incription.id !== undefined) {
      this.subscribeToSaveResponse(this.incriptionService.update(incription));
    } else {
      this.subscribeToSaveResponse(this.incriptionService.create(incription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncription>>): void {
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

  protected updateForm(incription: IIncription): void {
    this.editForm.patchValue({
      id: incription.id,
    });
  }

  protected createFromForm(): IIncription {
    return {
      ...new Incription(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
