import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrincipesDetailComponent } from './principes-detail.component';

describe('Principes Management Detail Component', () => {
  let comp: PrincipesDetailComponent;
  let fixture: ComponentFixture<PrincipesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrincipesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ principes: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrincipesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrincipesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load principes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.principes).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
