import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChoixService } from '../service/choix.service';
import { IChoix, Choix } from '../choix.model';

import { ChoixUpdateComponent } from './choix-update.component';

describe('Choix Management Update Component', () => {
  let comp: ChoixUpdateComponent;
  let fixture: ComponentFixture<ChoixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let choixService: ChoixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChoixUpdateComponent],
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
      .overrideTemplate(ChoixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChoixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    choixService = TestBed.inject(ChoixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const choix: IChoix = { id: 456 };

      activatedRoute.data = of({ choix });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(choix));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Choix>>();
      const choix = { id: 123 };
      jest.spyOn(choixService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ choix });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: choix }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(choixService.update).toHaveBeenCalledWith(choix);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Choix>>();
      const choix = new Choix();
      jest.spyOn(choixService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ choix });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: choix }));
      saveSubject.complete();

      // THEN
      expect(choixService.create).toHaveBeenCalledWith(choix);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Choix>>();
      const choix = { id: 123 };
      jest.spyOn(choixService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ choix });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(choixService.update).toHaveBeenCalledWith(choix);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
