import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AbonneService } from '../service/abonne.service';
import { IAbonne, Abonne } from '../abonne.model';
import { IGains } from 'app/entities/gains/gains.model';
import { GainsService } from 'app/entities/gains/service/gains.service';

import { AbonneUpdateComponent } from './abonne-update.component';

describe('Abonne Management Update Component', () => {
  let comp: AbonneUpdateComponent;
  let fixture: ComponentFixture<AbonneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let abonneService: AbonneService;
  let gainsService: GainsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AbonneUpdateComponent],
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
      .overrideTemplate(AbonneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AbonneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    abonneService = TestBed.inject(AbonneService);
    gainsService = TestBed.inject(GainsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Gains query and add missing value', () => {
      const abonne: IAbonne = { id: 456 };
      const gains: IGains[] = [{ id: 33647 }];
      abonne.gains = gains;

      const gainsCollection: IGains[] = [{ id: 41890 }];
      jest.spyOn(gainsService, 'query').mockReturnValue(of(new HttpResponse({ body: gainsCollection })));
      const additionalGains = [...gains];
      const expectedCollection: IGains[] = [...additionalGains, ...gainsCollection];
      jest.spyOn(gainsService, 'addGainsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ abonne });
      comp.ngOnInit();

      expect(gainsService.query).toHaveBeenCalled();
      expect(gainsService.addGainsToCollectionIfMissing).toHaveBeenCalledWith(gainsCollection, ...additionalGains);
      expect(comp.gainsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const abonne: IAbonne = { id: 456 };
      const gains: IGains = { id: 94710 };
      abonne.gains = [gains];

      activatedRoute.data = of({ abonne });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(abonne));
      expect(comp.gainsSharedCollection).toContain(gains);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Abonne>>();
      const abonne = { id: 123 };
      jest.spyOn(abonneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ abonne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: abonne }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(abonneService.update).toHaveBeenCalledWith(abonne);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Abonne>>();
      const abonne = new Abonne();
      jest.spyOn(abonneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ abonne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: abonne }));
      saveSubject.complete();

      // THEN
      expect(abonneService.create).toHaveBeenCalledWith(abonne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Abonne>>();
      const abonne = { id: 123 };
      jest.spyOn(abonneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ abonne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(abonneService.update).toHaveBeenCalledWith(abonne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackGainsById', () => {
      it('Should return tracked Gains primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGainsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedGains', () => {
      it('Should return option if no Gains is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedGains(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Gains for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedGains(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Gains is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedGains(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
