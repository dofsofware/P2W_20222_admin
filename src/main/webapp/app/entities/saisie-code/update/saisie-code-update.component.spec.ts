import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SaisieCodeService } from '../service/saisie-code.service';
import { ISaisieCode, SaisieCode } from '../saisie-code.model';

import { SaisieCodeUpdateComponent } from './saisie-code-update.component';

describe('SaisieCode Management Update Component', () => {
  let comp: SaisieCodeUpdateComponent;
  let fixture: ComponentFixture<SaisieCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let saisieCodeService: SaisieCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SaisieCodeUpdateComponent],
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
      .overrideTemplate(SaisieCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SaisieCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    saisieCodeService = TestBed.inject(SaisieCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const saisieCode: ISaisieCode = { id: 456 };

      activatedRoute.data = of({ saisieCode });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(saisieCode));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SaisieCode>>();
      const saisieCode = { id: 123 };
      jest.spyOn(saisieCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saisieCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: saisieCode }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(saisieCodeService.update).toHaveBeenCalledWith(saisieCode);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SaisieCode>>();
      const saisieCode = new SaisieCode();
      jest.spyOn(saisieCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saisieCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: saisieCode }));
      saveSubject.complete();

      // THEN
      expect(saisieCodeService.create).toHaveBeenCalledWith(saisieCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SaisieCode>>();
      const saisieCode = { id: 123 };
      jest.spyOn(saisieCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saisieCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(saisieCodeService.update).toHaveBeenCalledWith(saisieCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
