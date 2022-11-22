import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IncriptionService } from '../service/incription.service';
import { IIncription, Incription } from '../incription.model';

import { IncriptionUpdateComponent } from './incription-update.component';

describe('Incription Management Update Component', () => {
  let comp: IncriptionUpdateComponent;
  let fixture: ComponentFixture<IncriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incriptionService: IncriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IncriptionUpdateComponent],
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
      .overrideTemplate(IncriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incriptionService = TestBed.inject(IncriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const incription: IIncription = { id: 456 };

      activatedRoute.data = of({ incription });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(incription));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Incription>>();
      const incription = { id: 123 };
      jest.spyOn(incriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incription }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(incriptionService.update).toHaveBeenCalledWith(incription);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Incription>>();
      const incription = new Incription();
      jest.spyOn(incriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incription }));
      saveSubject.complete();

      // THEN
      expect(incriptionService.create).toHaveBeenCalledWith(incription);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Incription>>();
      const incription = { id: 123 };
      jest.spyOn(incriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incriptionService.update).toHaveBeenCalledWith(incription);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
