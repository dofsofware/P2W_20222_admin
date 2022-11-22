import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIncription, Incription } from '../incription.model';

import { IncriptionService } from './incription.service';

describe('Incription Service', () => {
  let service: IncriptionService;
  let httpMock: HttpTestingController;
  let elemDefault: IIncription;
  let expectedResult: IIncription | IIncription[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IncriptionService);
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

    it('should create a Incription', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Incription()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Incription', () => {
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

    it('should partial update a Incription', () => {
      const patchObject = Object.assign({}, new Incription());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Incription', () => {
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

    it('should delete a Incription', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIncriptionToCollectionIfMissing', () => {
      it('should add a Incription to an empty array', () => {
        const incription: IIncription = { id: 123 };
        expectedResult = service.addIncriptionToCollectionIfMissing([], incription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incription);
      });

      it('should not add a Incription to an array that contains it', () => {
        const incription: IIncription = { id: 123 };
        const incriptionCollection: IIncription[] = [
          {
            ...incription,
          },
          { id: 456 },
        ];
        expectedResult = service.addIncriptionToCollectionIfMissing(incriptionCollection, incription);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Incription to an array that doesn't contain it", () => {
        const incription: IIncription = { id: 123 };
        const incriptionCollection: IIncription[] = [{ id: 456 }];
        expectedResult = service.addIncriptionToCollectionIfMissing(incriptionCollection, incription);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incription);
      });

      it('should add only unique Incription to an array', () => {
        const incriptionArray: IIncription[] = [{ id: 123 }, { id: 456 }, { id: 84048 }];
        const incriptionCollection: IIncription[] = [{ id: 123 }];
        expectedResult = service.addIncriptionToCollectionIfMissing(incriptionCollection, ...incriptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incription: IIncription = { id: 123 };
        const incription2: IIncription = { id: 456 };
        expectedResult = service.addIncriptionToCollectionIfMissing([], incription, incription2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incription);
        expect(expectedResult).toContain(incription2);
      });

      it('should accept null and undefined values', () => {
        const incription: IIncription = { id: 123 };
        expectedResult = service.addIncriptionToCollectionIfMissing([], null, incription, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incription);
      });

      it('should return initial array if no Incription is added', () => {
        const incriptionCollection: IIncription[] = [{ id: 123 }];
        expectedResult = service.addIncriptionToCollectionIfMissing(incriptionCollection, undefined, null);
        expect(expectedResult).toEqual(incriptionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
