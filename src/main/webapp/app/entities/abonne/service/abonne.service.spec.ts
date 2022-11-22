import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Niveau } from 'app/entities/enumerations/niveau.model';
import { IAbonne, Abonne } from '../abonne.model';

import { AbonneService } from './abonne.service';

describe('Abonne Service', () => {
  let service: AbonneService;
  let httpMock: HttpTestingController;
  let elemDefault: IAbonne;
  let expectedResult: IAbonne | IAbonne[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AbonneService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      identifiant: 'AAAAAAA',
      telephone: 'AAAAAAA',
      motDePasse: 'AAAAAAA',
      score: 0,
      niveau: Niveau.DEBUTANT,
      createdAt: currentDate,
      dernierePaticipation: currentDate,
      actif: false,
      codeRactivation: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          dernierePaticipation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Abonne', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          dernierePaticipation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          dernierePaticipation: currentDate,
        },
        returnedFromService
      );

      service.create(new Abonne()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Abonne', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          identifiant: 'BBBBBB',
          telephone: 'BBBBBB',
          motDePasse: 'BBBBBB',
          score: 1,
          niveau: 'BBBBBB',
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          dernierePaticipation: currentDate.format(DATE_TIME_FORMAT),
          actif: true,
          codeRactivation: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          dernierePaticipation: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Abonne', () => {
      const patchObject = Object.assign(
        {
          telephone: 'BBBBBB',
          motDePasse: 'BBBBBB',
          niveau: 'BBBBBB',
          actif: true,
          codeRactivation: 'BBBBBB',
        },
        new Abonne()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdAt: currentDate,
          dernierePaticipation: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Abonne', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          identifiant: 'BBBBBB',
          telephone: 'BBBBBB',
          motDePasse: 'BBBBBB',
          score: 1,
          niveau: 'BBBBBB',
          createdAt: currentDate.format(DATE_TIME_FORMAT),
          dernierePaticipation: currentDate.format(DATE_TIME_FORMAT),
          actif: true,
          codeRactivation: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
          dernierePaticipation: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Abonne', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAbonneToCollectionIfMissing', () => {
      it('should add a Abonne to an empty array', () => {
        const abonne: IAbonne = { id: 123 };
        expectedResult = service.addAbonneToCollectionIfMissing([], abonne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(abonne);
      });

      it('should not add a Abonne to an array that contains it', () => {
        const abonne: IAbonne = { id: 123 };
        const abonneCollection: IAbonne[] = [
          {
            ...abonne,
          },
          { id: 456 },
        ];
        expectedResult = service.addAbonneToCollectionIfMissing(abonneCollection, abonne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Abonne to an array that doesn't contain it", () => {
        const abonne: IAbonne = { id: 123 };
        const abonneCollection: IAbonne[] = [{ id: 456 }];
        expectedResult = service.addAbonneToCollectionIfMissing(abonneCollection, abonne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(abonne);
      });

      it('should add only unique Abonne to an array', () => {
        const abonneArray: IAbonne[] = [{ id: 123 }, { id: 456 }, { id: 37018 }];
        const abonneCollection: IAbonne[] = [{ id: 123 }];
        expectedResult = service.addAbonneToCollectionIfMissing(abonneCollection, ...abonneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const abonne: IAbonne = { id: 123 };
        const abonne2: IAbonne = { id: 456 };
        expectedResult = service.addAbonneToCollectionIfMissing([], abonne, abonne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(abonne);
        expect(expectedResult).toContain(abonne2);
      });

      it('should accept null and undefined values', () => {
        const abonne: IAbonne = { id: 123 };
        expectedResult = service.addAbonneToCollectionIfMissing([], null, abonne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(abonne);
      });

      it('should return initial array if no Abonne is added', () => {
        const abonneCollection: IAbonne[] = [{ id: 123 }];
        expectedResult = service.addAbonneToCollectionIfMissing(abonneCollection, undefined, null);
        expect(expectedResult).toEqual(abonneCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
