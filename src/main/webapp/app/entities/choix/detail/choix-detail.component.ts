import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChoix } from '../choix.model';

@Component({
  selector: 'jhi-choix-detail',
  templateUrl: './choix-detail.component.html',
})
export class ChoixDetailComponent implements OnInit {
  choix: IChoix | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ choix }) => {
      this.choix = choix;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
