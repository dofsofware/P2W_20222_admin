import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISaisieCode, SaisieCode } from '../saisie-code.model';

import { SaisieCodeService } from './saisie-code.service';

describe('SaisieCode Service', () => {
  let service: SaisieCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISaisieCode;
  let expectedResult: ISaisieCode | ISaisieCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SaisieCodeService);
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

    it('should create a SaisieCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SaisieCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SaisieCode', () => {
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

    it('should partial update a SaisieCode', () => {
      const patchObject = Object.assign({}, new SaisieCode());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SaisieCode', () => {
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

    it('should delete a SaisieCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSaisieCodeToCollectionIfMissing', () => {
      it('should add a SaisieCode to an empty array', () => {
        const saisieCode: ISaisieCode = { id: 123 };
        expectedResult = service.addSaisieCodeToCollectionIfMissing([], saisieCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(saisieCode);
      });

      it('should not add a SaisieCode to an array that contains it', () => {
        const saisieCode: ISaisieCode = { id: 123 };
        const saisieCodeCollection: ISaisieCode[] = [
          {
            ...saisieCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addSaisieCodeToCollectionIfMissing(saisieCodeCollection, saisieCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SaisieCode to an array that doesn't contain it", () => {
        const saisieCode: ISaisieCode = { id: 123 };
        const saisieCodeCollection: ISaisieCode[] = [{ id: 456 }];
        expectedResult = service.addSaisieCodeToCollectionIfMissing(saisieCodeCollection, saisieCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(saisieCode);
      });

      it('should add only unique SaisieCode to an array', () => {
        const saisieCodeArray: ISaisieCode[] = [{ id: 123 }, { id: 456 }, { id: 23347 }];
        const saisieCodeCollection: ISaisieCode[] = [{ id: 123 }];
        expectedResult = service.addSaisieCodeToCollectionIfMissing(saisieCodeCollection, ...saisieCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const saisieCode: ISaisieCode = { id: 123 };
        const saisieCode2: ISaisieCode = { id: 456 };
        expectedResult = service.addSaisieCodeToCollectionIfMissing([], saisieCode, saisieCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(saisieCode);
        expect(expectedResult).toContain(saisieCode2);
      });

      it('should accept null and undefined values', () => {
        const saisieCode: ISaisieCode = { id: 123 };
        expectedResult = service.addSaisieCodeToCollectionIfMissing([], null, saisieCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(saisieCode);
      });

      it('should return initial array if no SaisieCode is added', () => {
        const saisieCodeCollection: ISaisieCode[] = [{ id: 123 }];
        expectedResult = service.addSaisieCodeToCollectionIfMissing(saisieCodeCollection, undefined, null);
        expect(expectedResult).toEqual(saisieCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
