import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResultatDetailComponent } from './resultat-detail.component';

describe('Resultat Management Detail Component', () => {
  let comp: ResultatDetailComponent;
  let fixture: ComponentFixture<ResultatDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResultatDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ resultat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ResultatDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ResultatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resultat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.resultat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
