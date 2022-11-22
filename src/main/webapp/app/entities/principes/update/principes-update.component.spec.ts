import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrincipesService } from '../service/principes.service';
import { IPrincipes, Principes } from '../principes.model';

import { PrincipesUpdateComponent } from './principes-update.component';

describe('Principes Management Update Component', () => {
  let comp: PrincipesUpdateComponent;
  let fixture: ComponentFixture<PrincipesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let principesService: PrincipesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrincipesUpdateComponent],
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
      .overrideTemplate(PrincipesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrincipesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    principesService = TestBed.inject(PrincipesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const principes: IPrincipes = { id: 456 };

      activatedRoute.data = of({ principes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(principes));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Principes>>();
      const principes = { id: 123 };
      jest.spyOn(principesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ principes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: principes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(principesService.update).toHaveBeenCalledWith(principes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Principes>>();
      const principes = new Principes();
      jest.spyOn(principesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ principes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: principes }));
      saveSubject.complete();

      // THEN
      expect(principesService.create).toHaveBeenCalledWith(principes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Principes>>();
      const principes = { id: 123 };
      jest.spyOn(principesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ principes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(principesService.update).toHaveBeenCalledWith(principes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
