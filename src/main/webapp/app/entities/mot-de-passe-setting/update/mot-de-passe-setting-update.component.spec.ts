import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MotDePasseSettingService } from '../service/mot-de-passe-setting.service';
import { IMotDePasseSetting, MotDePasseSetting } from '../mot-de-passe-setting.model';

import { MotDePasseSettingUpdateComponent } from './mot-de-passe-setting-update.component';

describe('MotDePasseSetting Management Update Component', () => {
  let comp: MotDePasseSettingUpdateComponent;
  let fixture: ComponentFixture<MotDePasseSettingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let motDePasseSettingService: MotDePasseSettingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MotDePasseSettingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MotDePasseSettingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MotDePasseSettingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    motDePasseSettingService = TestBed.inject(MotDePasseSettingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const motDePasseSetting: IMotDePasseSetting = { id: 456 };

      activatedRoute.data = of({ motDePasseSetting });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(motDePasseSetting));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotDePasseSetting>>();
      const motDePasseSetting = { id: 123 };
      jest.spyOn(motDePasseSettingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motDePasseSetting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: motDePasseSetting }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(motDePasseSettingService.update).toHaveBeenCalledWith(motDePasseSetting);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotDePasseSetting>>();
      const motDePasseSetting = new MotDePasseSetting();
      jest.spyOn(motDePasseSettingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motDePasseSetting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: motDePasseSetting }));
      saveSubject.complete();

      // THEN
      expect(motDePasseSettingService.create).toHaveBeenCalledWith(motDePasseSetting);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotDePasseSetting>>();
      const motDePasseSetting = { id: 123 };
      jest.spyOn(motDePasseSettingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motDePasseSetting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(motDePasseSettingService.update).toHaveBeenCalledWith(motDePasseSetting);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
