import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ResultatService } from '../service/resultat.service';
import { IResultat, Resultat } from '../resultat.model';

import { ResultatUpdateComponent } from './resultat-update.component';

describe('Resultat Management Update Component', () => {
  let comp: ResultatUpdateComponent;
  let fixture: ComponentFixture<ResultatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resultatService: ResultatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ResultatUpdateComponent],
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
      .overrideTemplate(ResultatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResultatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resultatService = TestBed.inject(ResultatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const resultat: IResultat = { id: 456 };

      activatedRoute.data = of({ resultat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(resultat));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resultat>>();
      const resultat = { id: 123 };
      jest.spyOn(resultatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resultat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resultat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(resultatService.update).toHaveBeenCalledWith(resultat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resultat>>();
      const resultat = new Resultat();
      jest.spyOn(resultatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resultat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resultat }));
      saveSubject.complete();

      // THEN
      expect(resultatService.create).toHaveBeenCalledWith(resultat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resultat>>();
      const resultat = { id: 123 };
      jest.spyOn(resultatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resultat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resultatService.update).toHaveBeenCalledWith(resultat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
