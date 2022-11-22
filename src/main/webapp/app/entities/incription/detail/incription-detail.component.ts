import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIncription } from '../incription.model';

@Component({
  selector: 'jhi-incription-detail',
  templateUrl: './incription-detail.component.html',
})
export class IncriptionDetailComponent implements OnInit {
  incription: IIncription | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incription }) => {
      this.incription = incription;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
