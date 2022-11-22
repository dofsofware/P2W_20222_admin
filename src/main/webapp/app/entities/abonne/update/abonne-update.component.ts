import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAbonne, Abonne } from '../abonne.model';
import { AbonneService } from '../service/abonne.service';
import { IGains } from 'app/entities/gains/gains.model';
import { GainsService } from 'app/entities/gains/service/gains.service';
import { Niveau } from 'app/entities/enumerations/niveau.model';

@Component({
  selector: 'jhi-abonne-update',
  templateUrl: './abonne-update.component.html',
})
export class AbonneUpdateComponent implements OnInit {
  isSaving = false;
  niveauValues = Object.keys(Niveau);

  gainsSharedCollection: IGains[] = [];

  editForm = this.fb.group({
    id: [],
    identifiant: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    telephone: [null, [Validators.required, Validators.minLength(12), Validators.maxLength(12)]],
    motDePasse: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(70)]],
    score: [null, [Validators.required]],
    niveau: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    dernierePaticipation: [],
    actif: [null, [Validators.required]],
    codeRactivation: [],
    gains: [],
  });

  constructor(
    protected abonneService: AbonneService,
    protected gainsService: GainsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abonne }) => {
      if (abonne.id === undefined) {
        const today = dayjs().startOf('day');
        abonne.createdAt = today;
        abonne.dernierePaticipation = today;
      }

      this.updateForm(abonne);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const abonne = this.createFromForm();
    if (abonne.id !== undefined) {
      this.subscribeToSaveResponse(this.abonneService.update(abonne));
    } else {
      this.subscribeToSaveResponse(this.abonneService.create(abonne));
    }
  }

  trackGainsById(_index: number, item: IGains): number {
    return item.id!;
  }

  getSelectedGains(option: IGains, selectedVals?: IGains[]): IGains {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbonne>>): void {
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

  protected updateForm(abonne: IAbonne): void {
    this.editForm.patchValue({
      id: abonne.id,
      identifiant: abonne.identifiant,
      telephone: abonne.telephone,
      motDePasse: abonne.motDePasse,
      score: abonne.score,
      niveau: abonne.niveau,
      createdAt: abonne.createdAt ? abonne.createdAt.format(DATE_TIME_FORMAT) : null,
      dernierePaticipation: abonne.dernierePaticipation ? abonne.dernierePaticipation.format(DATE_TIME_FORMAT) : null,
      actif: abonne.actif,
      codeRactivation: abonne.codeRactivation,
      gains: abonne.gains,
    });

    this.gainsSharedCollection = this.gainsService.addGainsToCollectionIfMissing(this.gainsSharedCollection, ...(abonne.gains ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.gainsService
      .query()
      .pipe(map((res: HttpResponse<IGains[]>) => res.body ?? []))
      .pipe(map((gains: IGains[]) => this.gainsService.addGainsToCollectionIfMissing(gains, ...(this.editForm.get('gains')!.value ?? []))))
      .subscribe((gains: IGains[]) => (this.gainsSharedCollection = gains));
  }

  protected createFromForm(): IAbonne {
    return {
      ...new Abonne(),
      id: this.editForm.get(['id'])!.value,
      identifiant: this.editForm.get(['identifiant'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      motDePasse: this.editForm.get(['motDePasse'])!.value,
      score: this.editForm.get(['score'])!.value,
      niveau: this.editForm.get(['niveau'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      dernierePaticipation: this.editForm.get(['dernierePaticipation'])!.value
        ? dayjs(this.editForm.get(['dernierePaticipation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      actif: this.editForm.get(['actif'])!.value,
      codeRactivation: this.editForm.get(['codeRactivation'])!.value,
      gains: this.editForm.get(['gains'])!.value,
    };
  }
}
