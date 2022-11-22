import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RestaureDetailComponent } from './restaure-detail.component';

describe('Restaure Management Detail Component', () => {
  let comp: RestaureDetailComponent;
  let fixture: ComponentFixture<RestaureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestaureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ restaure: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RestaureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RestaureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load restaure on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.restaure).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
