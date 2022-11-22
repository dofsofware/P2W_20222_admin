import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AuthentificationDetailComponent } from './authentification-detail.component';

describe('Authentification Management Detail Component', () => {
  let comp: AuthentificationDetailComponent;
  let fixture: ComponentFixture<AuthentificationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AuthentificationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ authentification: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AuthentificationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AuthentificationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load authentification on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.authentification).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
