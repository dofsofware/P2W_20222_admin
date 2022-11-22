import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGains } from '../gains.model';

@Component({
  selector: 'jhi-gains-detail',
  templateUrl: './gains-detail.component.html',
})
export class GainsDetailComponent implements OnInit {
  gains: IGains | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gains }) => {
      this.gains = gains;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
