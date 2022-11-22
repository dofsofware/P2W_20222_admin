import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPlay, Play } from '../play.model';
import { PlayService } from '../service/play.service';

@Component({
  selector: 'jhi-play-update',
  templateUrl: './play-update.component.html',
})
export class PlayUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected playService: PlayService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ play }) => {
      this.updateForm(play);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const play = this.createFromForm();
    if (play.id !== undefined) {
      this.subscribeToSaveResponse(this.playService.update(play));
    } else {
      this.subscribeToSaveResponse(this.playService.create(play));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlay>>): void {
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

  protected updateForm(play: IPlay): void {
    this.editForm.patchValue({
      id: play.id,
    });
  }

  protected createFromForm(): IPlay {
    return {
      ...new Play(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
