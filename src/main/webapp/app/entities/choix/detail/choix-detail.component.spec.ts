import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChoixDetailComponent } from './choix-detail.component';

describe('Choix Management Detail Component', () => {
  let comp: ChoixDetailComponent;
  let fixture: ComponentFixture<ChoixDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChoixDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ choix: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ChoixDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChoixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load choix on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.choix).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
