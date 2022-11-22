import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InfosAbonneService } from '../service/infos-abonne.service';
import { IInfosAbonne, InfosAbonne } from '../infos-abonne.model';

import { InfosAbonneUpdateComponent } from './infos-abonne-update.component';

describe('InfosAbonne Management Update Component', () => {
  let comp: InfosAbonneUpdateComponent;
  let fixture: ComponentFixture<InfosAbonneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let infosAbonneService: InfosAbonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InfosAbonneUpdateComponent],
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
      .overrideTemplate(InfosAbonneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InfosAbonneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    infosAbonneService = TestBed.inject(InfosAbonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const infosAbonne: IInfosAbonne = { id: 456 };

      activatedRoute.data = of({ infosAbonne });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(infosAbonne));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfosAbonne>>();
      const infosAbonne = { id: 123 };
      jest.spyOn(infosAbonneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infosAbonne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infosAbonne }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(infosAbonneService.update).toHaveBeenCalledWith(infosAbonne);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfosAbonne>>();
      const infosAbonne = new InfosAbonne();
      jest.spyOn(infosAbonneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infosAbonne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infosAbonne }));
      saveSubject.complete();

      // THEN
      expect(infosAbonneService.create).toHaveBeenCalledWith(infosAbonne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfosAbonne>>();
      const infosAbonne = { id: 123 };
      jest.spyOn(infosAbonneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infosAbonne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(infosAbonneService.update).toHaveBeenCalledWith(infosAbonne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
