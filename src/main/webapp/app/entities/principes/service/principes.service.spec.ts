import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrincipes, Principes } from '../principes.model';

import { PrincipesService } from './principes.service';

describe('Principes Service', () => {
  let service: PrincipesService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrincipes;
  let expectedResult: IPrincipes | IPrincipes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrincipesService);
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

    it('should create a Principes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Principes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Principes', () => {
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

    it('should partial update a Principes', () => {
      const patchObject = Object.assign({}, new Principes());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Principes', () => {
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

    it('should delete a Principes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrincipesToCollectionIfMissing', () => {
      it('should add a Principes to an empty array', () => {
        const principes: IPrincipes = { id: 123 };
        expectedResult = service.addPrincipesToCollectionIfMissing([], principes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(principes);
      });

      it('should not add a Principes to an array that contains it', () => {
        const principes: IPrincipes = { id: 123 };
        const principesCollection: IPrincipes[] = [
          {
            ...principes,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrincipesToCollectionIfMissing(principesCollection, principes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Principes to an array that doesn't contain it", () => {
        const principes: IPrincipes = { id: 123 };
        const principesCollection: IPrincipes[] = [{ id: 456 }];
        expectedResult = service.addPrincipesToCollectionIfMissing(principesCollection, principes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(principes);
      });

      it('should add only unique Principes to an array', () => {
        const principesArray: IPrincipes[] = [{ id: 123 }, { id: 456 }, { id: 34602 }];
        const principesCollection: IPrincipes[] = [{ id: 123 }];
        expectedResult = service.addPrincipesToCollectionIfMissing(principesCollection, ...principesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const principes: IPrincipes = { id: 123 };
        const principes2: IPrincipes = { id: 456 };
        expectedResult = service.addPrincipesToCollectionIfMissing([], principes, principes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(principes);
        expect(expectedResult).toContain(principes2);
      });

      it('should accept null and undefined values', () => {
        const principes: IPrincipes = { id: 123 };
        expectedResult = service.addPrincipesToCollectionIfMissing([], null, principes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(principes);
      });

      it('should return initial array if no Principes is added', () => {
        const principesCollection: IPrincipes[] = [{ id: 123 }];
        expectedResult = service.addPrincipesToCollectionIfMissing(principesCollection, undefined, null);
        expect(expectedResult).toEqual(principesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
