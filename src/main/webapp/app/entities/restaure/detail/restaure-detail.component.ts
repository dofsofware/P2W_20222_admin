import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRestaure } from '../restaure.model';

@Component({
  selector: 'jhi-restaure-detail',
  templateUrl: './restaure-detail.component.html',
})
export class RestaureDetailComponent implements OnInit {
  restaure: IRestaure | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaure }) => {
      this.restaure = restaure;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
