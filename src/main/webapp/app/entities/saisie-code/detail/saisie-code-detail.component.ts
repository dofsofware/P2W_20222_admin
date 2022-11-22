import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISaisieCode } from '../saisie-code.model';

@Component({
  selector: 'jhi-saisie-code-detail',
  templateUrl: './saisie-code-detail.component.html',
})
export class SaisieCodeDetailComponent implements OnInit {
  saisieCode: ISaisieCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ saisieCode }) => {
      this.saisieCode = saisieCode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
