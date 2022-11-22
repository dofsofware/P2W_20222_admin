import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GainsDetailComponent } from './gains-detail.component';

describe('Gains Management Detail Component', () => {
  let comp: GainsDetailComponent;
  let fixture: ComponentFixture<GainsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GainsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gains: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GainsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GainsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gains on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gains).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
