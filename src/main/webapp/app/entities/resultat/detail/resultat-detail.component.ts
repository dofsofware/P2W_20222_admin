import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResultat } from '../resultat.model';

@Component({
  selector: 'jhi-resultat-detail',
  templateUrl: './resultat-detail.component.html',
})
export class ResultatDetailComponent implements OnInit {
  resultat: IResultat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultat }) => {
      this.resultat = resultat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
