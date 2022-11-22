import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IGains, Gains } from '../gains.model';
import { GainsService } from '../service/gains.service';

@Component({
  selector: 'jhi-gains-update',
  templateUrl: './gains-update.component.html',
})
export class GainsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    telephone: [null, [Validators.required, Validators.minLength(12), Validators.maxLength(12)]],
    minute: [null, [Validators.required]],
    megabit: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
  });

  constructor(protected gainsService: GainsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gains }) => {
      if (gains.id === undefined) {
        const today = dayjs().startOf('day');
        gains.createdAt = today;
      }

      this.updateForm(gains);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gains = this.createFromForm();
    if (gains.id !== undefined) {
      this.subscribeToSaveResponse(this.gainsService.update(gains));
    } else {
      this.subscribeToSaveResponse(this.gainsService.create(gains));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGains>>): void {
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

  protected updateForm(gains: IGains): void {
    this.editForm.patchValue({
      id: gains.id,
      telephone: gains.telephone,
      minute: gains.minute,
      megabit: gains.megabit,
      createdAt: gains.createdAt ? gains.createdAt.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IGains {
    return {
      ...new Gains(),
      id: this.editForm.get(['id'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      minute: this.editForm.get(['minute'])!.value,
      megabit: this.editForm.get(['megabit'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
