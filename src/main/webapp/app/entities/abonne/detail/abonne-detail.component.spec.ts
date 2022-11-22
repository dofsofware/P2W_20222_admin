import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AbonneDetailComponent } from './abonne-detail.component';

describe('Abonne Management Detail Component', () => {
  let comp: AbonneDetailComponent;
  let fixture: ComponentFixture<AbonneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AbonneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ abonne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AbonneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AbonneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load abonne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.abonne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
