import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IInfosAbonne, InfosAbonne } from '../infos-abonne.model';
import { InfosAbonneService } from '../service/infos-abonne.service';

@Component({
  selector: 'jhi-infos-abonne-update',
  templateUrl: './infos-abonne-update.component.html',
})
export class InfosAbonneUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected infosAbonneService: InfosAbonneService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infosAbonne }) => {
      this.updateForm(infosAbonne);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infosAbonne = this.createFromForm();
    if (infosAbonne.id !== undefined) {
      this.subscribeToSaveResponse(this.infosAbonneService.update(infosAbonne));
    } else {
      this.subscribeToSaveResponse(this.infosAbonneService.create(infosAbonne));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfosAbonne>>): void {
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

  protected updateForm(infosAbonne: IInfosAbonne): void {
    this.editForm.patchValue({
      id: infosAbonne.id,
    });
  }

  protected createFromForm(): IInfosAbonne {
    return {
      ...new InfosAbonne(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
