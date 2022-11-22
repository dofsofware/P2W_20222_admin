import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAbonne } from '../abonne.model';
import { AbonneService } from '../service/abonne.service';

@Component({
  selector: 'jhi-abonne-detail',
  templateUrl: './abonne-detail.component.html',
})
export class AbonneDetailComponent implements OnInit {
  abonne: IAbonne | null = null;
  abonnes: IAbonne[] = [];
  MonRang: number;

  constructor(protected activatedRoute: ActivatedRoute, protected abonneService: AbonneService) {
    this.MonRang = 0;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abonne }) => {
      this.abonne = abonne;
    });
  }

  previousState(): void {
    window.history.back();
  }
  getRang(): number {
    this.abonnes = [];
    this.abonneService.query().subscribe((res: HttpResponse<IAbonne[]>) => {
      if (res.body) {
        this.abonnes = [];
        for (let i = 0; i < res.body.length; i++) {
          this.abonnes.push(res.body[i]);
        }
      }

      this.abonnes.sort(function (a: IAbonne, b: IAbonne) {
        return b.score! - a.score!;
      });
      const idencore = this.abonne?.id;
      this.MonRang = this.abonnes.findIndex(a => a.id === idencore) + 1;
    });
    return this.MonRang;
  }

  getRangIndex(): string {
    if (this.MonRang === 1) {
      return 'er';
    } else if (this.getRang()! >= 2) {
      return 'ème';
    } else {
      return '';
    }
  }
  haveData(): boolean {
    if (this.abonne?.gains?.length === 0) {
      return false;
    } else {
      return true;
    }
  }
}
