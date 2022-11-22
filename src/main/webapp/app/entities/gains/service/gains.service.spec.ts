import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGains, Gains } from '../gains.model';

import { GainsService } from './gains.service';

describe('Gains Service', () => {
  let service: GainsService;
  let httpMock: HttpTestingController;
  let elemDefault: IGains;
  let expectedResult: IGains | IGains[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GainsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      telephone: 'AAAAAAA',
      minute: 0,
      megabit: 0,
      createdAt: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Gains', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.create(new Gains()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Gains', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          telephone: 'BBBBBB',
          minute: 1,
          megabit: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Gains', () => {
      const patchObject = Object.assign(
        {
          minute: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        new Gains()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Gains', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          telephone: 'BBBBBB',
          minute: 1,
          megabit: 1,
          createdAt: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdAt: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Gains', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGainsToCollectionIfMissing', () => {
      it('should add a Gains to an empty array', () => {
        const gains: IGains = { id: 123 };
        expectedResult = service.addGainsToCollectionIfMissing([], gains);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gains);
      });

      it('should not add a Gains to an array that contains it', () => {
        const gains: IGains = { id: 123 };
        const gainsCollection: IGains[] = [
          {
            ...gains,
          },
          { id: 456 },
        ];
        expectedResult = service.addGainsToCollectionIfMissing(gainsCollection, gains);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Gains to an array that doesn't contain it", () => {
        const gains: IGains = { id: 123 };
        const gainsCollection: IGains[] = [{ id: 456 }];
        expectedResult = service.addGainsToCollectionIfMissing(gainsCollection, gains);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gains);
      });

      it('should add only unique Gains to an array', () => {
        const gainsArray: IGains[] = [{ id: 123 }, { id: 456 }, { id: 92439 }];
        const gainsCollection: IGains[] = [{ id: 123 }];
        expectedResult = service.addGainsToCollectionIfMissing(gainsCollection, ...gainsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gains: IGains = { id: 123 };
        const gains2: IGains = { id: 456 };
        expectedResult = service.addGainsToCollectionIfMissing([], gains, gains2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gains);
        expect(expectedResult).toContain(gains2);
      });

      it('should accept null and undefined values', () => {
        const gains: IGains = { id: 123 };
        expectedResult = service.addGainsToCollectionIfMissing([], null, gains, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gains);
      });

      it('should return initial array if no Gains is added', () => {
        const gainsCollection: IGains[] = [{ id: 123 }];
        expectedResult = service.addGainsToCollectionIfMissing(gainsCollection, undefined, null);
        expect(expectedResult).toEqual(gainsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
