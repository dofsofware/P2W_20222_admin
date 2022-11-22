import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRestaure, Restaure } from '../restaure.model';

import { RestaureService } from './restaure.service';

describe('Restaure Service', () => {
  let service: RestaureService;
  let httpMock: HttpTestingController;
  let elemDefault: IRestaure;
  let expectedResult: IRestaure | IRestaure[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestaureService);
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

    it('should create a Restaure', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Restaure()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Restaure', () => {
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

    it('should partial update a Restaure', () => {
      const patchObject = Object.assign({}, new Restaure());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Restaure', () => {
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

    it('should delete a Restaure', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestaureToCollectionIfMissing', () => {
      it('should add a Restaure to an empty array', () => {
        const restaure: IRestaure = { id: 123 };
        expectedResult = service.addRestaureToCollectionIfMissing([], restaure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaure);
      });

      it('should not add a Restaure to an array that contains it', () => {
        const restaure: IRestaure = { id: 123 };
        const restaureCollection: IRestaure[] = [
          {
            ...restaure,
          },
          { id: 456 },
        ];
        expectedResult = service.addRestaureToCollectionIfMissing(restaureCollection, restaure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Restaure to an array that doesn't contain it", () => {
        const restaure: IRestaure = { id: 123 };
        const restaureCollection: IRestaure[] = [{ id: 456 }];
        expectedResult = service.addRestaureToCollectionIfMissing(restaureCollection, restaure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaure);
      });

      it('should add only unique Restaure to an array', () => {
        const restaureArray: IRestaure[] = [{ id: 123 }, { id: 456 }, { id: 72603 }];
        const restaureCollection: IRestaure[] = [{ id: 123 }];
        expectedResult = service.addRestaureToCollectionIfMissing(restaureCollection, ...restaureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restaure: IRestaure = { id: 123 };
        const restaure2: IRestaure = { id: 456 };
        expectedResult = service.addRestaureToCollectionIfMissing([], restaure, restaure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaure);
        expect(expectedResult).toContain(restaure2);
      });

      it('should accept null and undefined values', () => {
        const restaure: IRestaure = { id: 123 };
        expectedResult = service.addRestaureToCollectionIfMissing([], null, restaure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaure);
      });

      it('should return initial array if no Restaure is added', () => {
        const restaureCollection: IRestaure[] = [{ id: 123 }];
        expectedResult = service.addRestaureToCollectionIfMissing(restaureCollection, undefined, null);
        expect(expectedResult).toEqual(restaureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
