import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IncriptionDetailComponent } from './incription-detail.component';

describe('Incription Management Detail Component', () => {
  let comp: IncriptionDetailComponent;
  let fixture: ComponentFixture<IncriptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IncriptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ incription: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IncriptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IncriptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load incription on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.incription).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
