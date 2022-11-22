import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrincipes } from '../principes.model';

@Component({
  selector: 'jhi-principes-detail',
  templateUrl: './principes-detail.component.html',
})
export class PrincipesDetailComponent implements OnInit {
  principes: IPrincipes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ principes }) => {
      this.principes = principes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
