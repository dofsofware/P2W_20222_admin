import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RecetteService } from '../service/recette.service';
import { IRecette, Recette } from '../recette.model';

import { RecetteUpdateComponent } from './recette-update.component';

describe('Recette Management Update Component', () => {
  let comp: RecetteUpdateComponent;
  let fixture: ComponentFixture<RecetteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recetteService: RecetteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RecetteUpdateComponent],
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
      .overrideTemplate(RecetteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecetteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recetteService = TestBed.inject(RecetteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const recette: IRecette = { id: 456 };

      activatedRoute.data = of({ recette });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(recette));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Recette>>();
      const recette = { id: 123 };
      jest.spyOn(recetteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recette });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recette }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(recetteService.update).toHaveBeenCalledWith(recette);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Recette>>();
      const recette = new Recette();
      jest.spyOn(recetteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recette });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recette }));
      saveSubject.complete();

      // THEN
      expect(recetteService.create).toHaveBeenCalledWith(recette);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Recette>>();
      const recette = { id: 123 };
      jest.spyOn(recetteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recette });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recetteService.update).toHaveBeenCalledWith(recette);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
