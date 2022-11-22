import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InfosAbonneDetailComponent } from './infos-abonne-detail.component';

describe('InfosAbonne Management Detail Component', () => {
  let comp: InfosAbonneDetailComponent;
  let fixture: ComponentFixture<InfosAbonneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InfosAbonneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ infosAbonne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InfosAbonneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InfosAbonneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load infosAbonne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.infosAbonne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
