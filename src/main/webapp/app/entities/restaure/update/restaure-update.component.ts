import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRestaure, Restaure } from '../restaure.model';
import { RestaureService } from '../service/restaure.service';

@Component({
  selector: 'jhi-restaure-update',
  templateUrl: './restaure-update.component.html',
})
export class RestaureUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected restaureService: RestaureService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaure }) => {
      this.updateForm(restaure);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaure = this.createFromForm();
    if (restaure.id !== undefined) {
      this.subscribeToSaveResponse(this.restaureService.update(restaure));
    } else {
      this.subscribeToSaveResponse(this.restaureService.create(restaure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaure>>): void {
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

  protected updateForm(restaure: IRestaure): void {
    this.editForm.patchValue({
      id: restaure.id,
    });
  }

  protected createFromForm(): IRestaure {
    return {
      ...new Restaure(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
