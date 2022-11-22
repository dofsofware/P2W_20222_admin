import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInfosAbonne, InfosAbonne } from '../infos-abonne.model';

import { InfosAbonneService } from './infos-abonne.service';

describe('InfosAbonne Service', () => {
  let service: InfosAbonneService;
  let httpMock: HttpTestingController;
  let elemDefault: IInfosAbonne;
  let expectedResult: IInfosAbonne | IInfosAbonne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InfosAbonneService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a InfosAbonne', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InfosAbonne()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InfosAbonne', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InfosAbonne', () => {
      const patchObject = Object.assign({}, new InfosAbonne());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InfosAbonne', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a InfosAbonne', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInfosAbonneToCollectionIfMissing', () => {
      it('should add a InfosAbonne to an empty array', () => {
        const infosAbonne: IInfosAbonne = { id: 123 };
        expectedResult = service.addInfosAbonneToCollectionIfMissing([], infosAbonne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infosAbonne);
      });

      it('should not add a InfosAbonne to an array that contains it', () => {
        const infosAbonne: IInfosAbonne = { id: 123 };
        const infosAbonneCollection: IInfosAbonne[] = [
          {
            ...infosAbonne,
          },
          { id: 456 },
        ];
        expectedResult = service.addInfosAbonneToCollectionIfMissing(infosAbonneCollection, infosAbonne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InfosAbonne to an array that doesn't contain it", () => {
        const infosAbonne: IInfosAbonne = { id: 123 };
        const infosAbonneCollection: IInfosAbonne[] = [{ id: 456 }];
        expectedResult = service.addInfosAbonneToCollectionIfMissing(infosAbonneCollection, infosAbonne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infosAbonne);
      });

      it('should add only unique InfosAbonne to an array', () => {
        const infosAbonneArray: IInfosAbonne[] = [{ id: 123 }, { id: 456 }, { id: 34391 }];
        const infosAbonneCollection: IInfosAbonne[] = [{ id: 123 }];
        expectedResult = service.addInfosAbonneToCollectionIfMissing(infosAbonneCollection, ...infosAbonneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const infosAbonne: IInfosAbonne = { id: 123 };
        const infosAbonne2: IInfosAbonne = { id: 456 };
        expectedResult = service.addInfosAbonneToCollectionIfMissing([], infosAbonne, infosAbonne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infosAbonne);
        expect(expectedResult).toContain(infosAbonne2);
      });

      it('should accept null and undefined values', () => {
        const infosAbonne: IInfosAbonne = { id: 123 };
        expectedResult = service.addInfosAbonneToCollectionIfMissing([], null, infosAbonne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infosAbonne);
      });

      it('should return initial array if no InfosAbonne is added', () => {
        const infosAbonneCollection: IInfosAbonne[] = [{ id: 123 }];
        expectedResult = service.addInfosAbonneToCollectionIfMissing(infosAbonneCollection, undefined, null);
        expect(expectedResult).toEqual(infosAbonneCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
