import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMotDePasseSetting } from '../mot-de-passe-setting.model';

@Component({
  selector: 'jhi-mot-de-passe-setting-detail',
  templateUrl: './mot-de-passe-setting-detail.component.html',
})
export class MotDePasseSettingDetailComponent implements OnInit {
  motDePasseSetting: IMotDePasseSetting | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ motDePasseSetting }) => {
      this.motDePasseSetting = motDePasseSetting;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
