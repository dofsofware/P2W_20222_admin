import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAuthentification, Authentification } from '../authentification.model';

import { AuthentificationService } from './authentification.service';

describe('Authentification Service', () => {
  let service: AuthentificationService;
  let httpMock: HttpTestingController;
  let elemDefault: IAuthentification;
  let expectedResult: IAuthentification | IAuthentification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AuthentificationService);
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

    it('should create a Authentification', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Authentification()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Authentification', () => {
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

    it('should partial update a Authentification', () => {
      const patchObject = Object.assign({}, new Authentification());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Authentification', () => {
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

    it('should delete a Authentification', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAuthentificationToCollectionIfMissing', () => {
      it('should add a Authentification to an empty array', () => {
        const authentification: IAuthentification = { id: 123 };
        expectedResult = service.addAuthentificationToCollectionIfMissing([], authentification);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(authentification);
      });

      it('should not add a Authentification to an array that contains it', () => {
        const authentification: IAuthentification = { id: 123 };
        const authentificationCollection: IAuthentification[] = [
          {
            ...authentification,
          },
          { id: 456 },
        ];
        expectedResult = service.addAuthentificationToCollectionIfMissing(authentificationCollection, authentification);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Authentification to an array that doesn't contain it", () => {
        const authentification: IAuthentification = { id: 123 };
        const authentificationCollection: IAuthentification[] = [{ id: 456 }];
        expectedResult = service.addAuthentificationToCollectionIfMissing(authentificationCollection, authentification);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(authentification);
      });

      it('should add only unique Authentification to an array', () => {
        const authentificationArray: IAuthentification[] = [{ id: 123 }, { id: 456 }, { id: 26130 }];
        const authentificationCollection: IAuthentification[] = [{ id: 123 }];
        expectedResult = service.addAuthentificationToCollectionIfMissing(authentificationCollection, ...authentificationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const authentification: IAuthentification = { id: 123 };
        const authentification2: IAuthentification = { id: 456 };
        expectedResult = service.addAuthentificationToCollectionIfMissing([], authentification, authentification2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(authentification);
        expect(expectedResult).toContain(authentification2);
      });

      it('should accept null and undefined values', () => {
        const authentification: IAuthentification = { id: 123 };
        expectedResult = service.addAuthentificationToCollectionIfMissing([], null, authentification, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(authentification);
      });

      it('should return initial array if no Authentification is added', () => {
        const authentificationCollection: IAuthentification[] = [{ id: 123 }];
        expectedResult = service.addAuthentificationToCollectionIfMissing(authentificationCollection, undefined, null);
        expect(expectedResult).toEqual(authentificationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
