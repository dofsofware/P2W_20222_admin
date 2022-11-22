import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResultat, Resultat } from '../resultat.model';

import { ResultatService } from './resultat.service';

describe('Resultat Service', () => {
  let service: ResultatService;
  let httpMock: HttpTestingController;
  let elemDefault: IResultat;
  let expectedResult: IResultat | IResultat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ResultatService);
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

    it('should create a Resultat', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Resultat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Resultat', () => {
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

    it('should partial update a Resultat', () => {
      const patchObject = Object.assign({}, new Resultat());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Resultat', () => {
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

    it('should delete a Resultat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addResultatToCollectionIfMissing', () => {
      it('should add a Resultat to an empty array', () => {
        const resultat: IResultat = { id: 123 };
        expectedResult = service.addResultatToCollectionIfMissing([], resultat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resultat);
      });

      it('should not add a Resultat to an array that contains it', () => {
        const resultat: IResultat = { id: 123 };
        const resultatCollection: IResultat[] = [
          {
            ...resultat,
          },
          { id: 456 },
        ];
        expectedResult = service.addResultatToCollectionIfMissing(resultatCollection, resultat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Resultat to an array that doesn't contain it", () => {
        const resultat: IResultat = { id: 123 };
        const resultatCollection: IResultat[] = [{ id: 456 }];
        expectedResult = service.addResultatToCollectionIfMissing(resultatCollection, resultat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resultat);
      });

      it('should add only unique Resultat to an array', () => {
        const resultatArray: IResultat[] = [{ id: 123 }, { id: 456 }, { id: 73492 }];
        const resultatCollection: IResultat[] = [{ id: 123 }];
        expectedResult = service.addResultatToCollectionIfMissing(resultatCollection, ...resultatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resultat: IResultat = { id: 123 };
        const resultat2: IResultat = { id: 456 };
        expectedResult = service.addResultatToCollectionIfMissing([], resultat, resultat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resultat);
        expect(expectedResult).toContain(resultat2);
      });

      it('should accept null and undefined values', () => {
        const resultat: IResultat = { id: 123 };
        expectedResult = service.addResultatToCollectionIfMissing([], null, resultat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resultat);
      });

      it('should return initial array if no Resultat is added', () => {
        const resultatCollection: IResultat[] = [{ id: 123 }];
        expectedResult = service.addResultatToCollectionIfMissing(resultatCollection, undefined, null);
        expect(expectedResult).toEqual(resultatCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
