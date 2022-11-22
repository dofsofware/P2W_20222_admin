import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRecette, Recette } from '../recette.model';
import { RecetteService } from '../service/recette.service';
import { ChoixDuGain } from 'app/entities/enumerations/choix-du-gain.model';

@Component({
  selector: 'jhi-recette-update',
  templateUrl: './recette-update.component.html',
})
export class RecetteUpdateComponent implements OnInit {
  isSaving = false;
  choixDuGainValues = Object.keys(ChoixDuGain);

  editForm = this.fb.group({
    id: [],
    telephone: [null, [Validators.required, Validators.minLength(12), Validators.maxLength(12)]],
    createdAt: [null, [Validators.required]],
    montant: [null, [Validators.required]],
    choixDuGain: [null, [Validators.required]],
  });

  constructor(protected recetteService: RecetteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recette }) => {
      if (recette.id === undefined) {
        const today = dayjs().startOf('day');
        recette.createdAt = today;
      }

      this.updateForm(recette);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recette = this.createFromForm();
    if (recette.id !== undefined) {
      this.subscribeToSaveResponse(this.recetteService.update(recette));
    } else {
      this.subscribeToSaveResponse(this.recetteService.create(recette));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecette>>): void {
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

  protected updateForm(recette: IRecette): void {
    this.editForm.patchValue({
      id: recette.id,
      telephone: recette.telephone,
      createdAt: recette.createdAt ? recette.createdAt.format(DATE_TIME_FORMAT) : null,
      montant: recette.montant,
      choixDuGain: recette.choixDuGain,
    });
  }

  protected createFromForm(): IRecette {
    return {
      ...new Recette(),
      id: this.editForm.get(['id'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      montant: this.editForm.get(['montant'])!.value,
      choixDuGain: this.editForm.get(['choixDuGain'])!.value,
    };
  }
}
