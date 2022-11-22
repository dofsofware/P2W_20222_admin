import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAuthentification } from '../authentification.model';

@Component({
  selector: 'jhi-authentification-detail',
  templateUrl: './authentification-detail.component.html',
})
export class AuthentificationDetailComponent implements OnInit {
  authentification: IAuthentification | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authentification }) => {
      this.authentification = authentification;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
