import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IChoix, Choix } from '../choix.model';

import { ChoixService } from './choix.service';

describe('Choix Service', () => {
  let service: ChoixService;
  let httpMock: HttpTestingController;
  let elemDefault: IChoix;
  let expectedResult: IChoix | IChoix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChoixService);
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

    it('should create a Choix', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Choix()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Choix', () => {
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

    it('should partial update a Choix', () => {
      const patchObject = Object.assign({}, new Choix());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Choix', () => {
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

    it('should delete a Choix', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addChoixToCollectionIfMissing', () => {
      it('should add a Choix to an empty array', () => {
        const choix: IChoix = { id: 123 };
        expectedResult = service.addChoixToCollectionIfMissing([], choix);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(choix);
      });

      it('should not add a Choix to an array that contains it', () => {
        const choix: IChoix = { id: 123 };
        const choixCollection: IChoix[] = [
          {
            ...choix,
          },
          { id: 456 },
        ];
        expectedResult = service.addChoixToCollectionIfMissing(choixCollection, choix);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Choix to an array that doesn't contain it", () => {
        const choix: IChoix = { id: 123 };
        const choixCollection: IChoix[] = [{ id: 456 }];
        expectedResult = service.addChoixToCollectionIfMissing(choixCollection, choix);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(choix);
      });

      it('should add only unique Choix to an array', () => {
        const choixArray: IChoix[] = [{ id: 123 }, { id: 456 }, { id: 86318 }];
        const choixCollection: IChoix[] = [{ id: 123 }];
        expectedResult = service.addChoixToCollectionIfMissing(choixCollection, ...choixArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const choix: IChoix = { id: 123 };
        const choix2: IChoix = { id: 456 };
        expectedResult = service.addChoixToCollectionIfMissing([], choix, choix2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(choix);
        expect(expectedResult).toContain(choix2);
      });

      it('should accept null and undefined values', () => {
        const choix: IChoix = { id: 123 };
        expectedResult = service.addChoixToCollectionIfMissing([], null, choix, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(choix);
      });

      it('should return initial array if no Choix is added', () => {
        const choixCollection: IChoix[] = [{ id: 123 }];
        expectedResult = service.addChoixToCollectionIfMissing(choixCollection, undefined, null);
        expect(expectedResult).toEqual(choixCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
