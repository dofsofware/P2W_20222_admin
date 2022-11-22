import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MotDePasseSettingDetailComponent } from './mot-de-passe-setting-detail.component';

describe('MotDePasseSetting Management Detail Component', () => {
  let comp: MotDePasseSettingDetailComponent;
  let fixture: ComponentFixture<MotDePasseSettingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MotDePasseSettingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ motDePasseSetting: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MotDePasseSettingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MotDePasseSettingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load motDePasseSetting on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.motDePasseSetting).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
