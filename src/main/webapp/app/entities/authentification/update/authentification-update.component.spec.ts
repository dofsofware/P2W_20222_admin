import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AuthentificationService } from '../service/authentification.service';
import { IAuthentification, Authentification } from '../authentification.model';

import { AuthentificationUpdateComponent } from './authentification-update.component';

describe('Authentification Management Update Component', () => {
  let comp: AuthentificationUpdateComponent;
  let fixture: ComponentFixture<AuthentificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let authentificationService: AuthentificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AuthentificationUpdateComponent],
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
      .overrideTemplate(AuthentificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AuthentificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    authentificationService = TestBed.inject(AuthentificationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const authentification: IAuthentification = { id: 456 };

      activatedRoute.data = of({ authentification });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(authentification));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Authentification>>();
      const authentification = { id: 123 };
      jest.spyOn(authentificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authentification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: authentification }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(authentificationService.update).toHaveBeenCalledWith(authentification);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Authentification>>();
      const authentification = new Authentification();
      jest.spyOn(authentificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authentification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: authentification }));
      saveSubject.complete();

      // THEN
      expect(authentificationService.create).toHaveBeenCalledWith(authentification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Authentification>>();
      const authentification = { id: 123 };
      jest.spyOn(authentificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authentification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(authentificationService.update).toHaveBeenCalledWith(authentification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
