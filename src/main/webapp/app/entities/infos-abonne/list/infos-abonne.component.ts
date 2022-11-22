import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfosAbonne } from '../infos-abonne.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { InfosAbonneService } from '../service/infos-abonne.service';
import { InfosAbonneDeleteDialogComponent } from '../delete/infos-abonne-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-infos-abonne',
  templateUrl: './infos-abonne.component.html',
})
export class InfosAbonneComponent implements OnInit {
  infosAbonnes: IInfosAbonne[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected infosAbonneService: InfosAbonneService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.infosAbonnes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.infosAbonneService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IInfosAbonne[]>) => {
          this.isLoading = false;
          this.paginateInfosAbonnes(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.infosAbonnes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IInfosAbonne): number {
    return item.id!;
  }

  delete(infosAbonne: IInfosAbonne): void {
    const modalRef = this.modalService.open(InfosAbonneDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.infosAbonne = infosAbonne;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInfosAbonnes(data: IInfosAbonne[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.infosAbonnes.push(d);
      }
    }
  }
}
