import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GainsService } from '../service/gains.service';
import { IGains, Gains } from '../gains.model';

import { GainsUpdateComponent } from './gains-update.component';

describe('Gains Management Update Component', () => {
  let comp: GainsUpdateComponent;
  let fixture: ComponentFixture<GainsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gainsService: GainsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GainsUpdateComponent],
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
      .overrideTemplate(GainsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GainsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gainsService = TestBed.inject(GainsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gains: IGains = { id: 456 };

      activatedRoute.data = of({ gains });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(gains));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gains>>();
      const gains = { id: 123 };
      jest.spyOn(gainsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gains });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gains }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(gainsService.update).toHaveBeenCalledWith(gains);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gains>>();
      const gains = new Gains();
      jest.spyOn(gainsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gains });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gains }));
      saveSubject.complete();

      // THEN
      expect(gainsService.create).toHaveBeenCalledWith(gains);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gains>>();
      const gains = { id: 123 };
      jest.spyOn(gainsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gains });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gainsService.update).toHaveBeenCalledWith(gains);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
