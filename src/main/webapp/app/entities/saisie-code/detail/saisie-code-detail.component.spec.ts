import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SaisieCodeDetailComponent } from './saisie-code-detail.component';

describe('SaisieCode Management Detail Component', () => {
  let comp: SaisieCodeDetailComponent;
  let fixture: ComponentFixture<SaisieCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SaisieCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ saisieCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SaisieCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SaisieCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load saisieCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.saisieCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
