import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfosAbonne } from '../infos-abonne.model';

@Component({
  selector: 'jhi-infos-abonne-detail',
  templateUrl: './infos-abonne-detail.component.html',
})
export class InfosAbonneDetailComponent implements OnInit {
  infosAbonne: IInfosAbonne | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infosAbonne }) => {
      this.infosAbonne = infosAbonne;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
