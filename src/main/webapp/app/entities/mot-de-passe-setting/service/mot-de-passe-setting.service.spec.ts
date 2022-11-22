import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMotDePasseSetting, MotDePasseSetting } from '../mot-de-passe-setting.model';

import { MotDePasseSettingService } from './mot-de-passe-setting.service';

describe('MotDePasseSetting Service', () => {
  let service: MotDePasseSettingService;
  let httpMock: HttpTestingController;
  let elemDefault: IMotDePasseSetting;
  let expectedResult: IMotDePasseSetting | IMotDePasseSetting[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MotDePasseSettingService);
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

    it('should create a MotDePasseSetting', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MotDePasseSetting()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MotDePasseSetting', () => {
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

    it('should partial update a MotDePasseSetting', () => {
      const patchObject = Object.assign({}, new MotDePasseSetting());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MotDePasseSetting', () => {
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

    it('should delete a MotDePasseSetting', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMotDePasseSettingToCollectionIfMissing', () => {
      it('should add a MotDePasseSetting to an empty array', () => {
        const motDePasseSetting: IMotDePasseSetting = { id: 123 };
        expectedResult = service.addMotDePasseSettingToCollectionIfMissing([], motDePasseSetting);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(motDePasseSetting);
      });

      it('should not add a MotDePasseSetting to an array that contains it', () => {
        const motDePasseSetting: IMotDePasseSetting = { id: 123 };
        const motDePasseSettingCollection: IMotDePasseSetting[] = [
          {
            ...motDePasseSetting,
          },
          { id: 456 },
        ];
        expectedResult = service.addMotDePasseSettingToCollectionIfMissing(motDePasseSettingCollection, motDePasseSetting);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MotDePasseSetting to an array that doesn't contain it", () => {
        const motDePasseSetting: IMotDePasseSetting = { id: 123 };
        const motDePasseSettingCollection: IMotDePasseSetting[] = [{ id: 456 }];
        expectedResult = service.addMotDePasseSettingToCollectionIfMissing(motDePasseSettingCollection, motDePasseSetting);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(motDePasseSetting);
      });

      it('should add only unique MotDePasseSetting to an array', () => {
        const motDePasseSettingArray: IMotDePasseSetting[] = [{ id: 123 }, { id: 456 }, { id: 46940 }];
        const motDePasseSettingCollection: IMotDePasseSetting[] = [{ id: 123 }];
        expectedResult = service.addMotDePasseSettingToCollectionIfMissing(motDePasseSettingCollection, ...motDePasseSettingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const motDePasseSetting: IMotDePasseSetting = { id: 123 };
        const motDePasseSetting2: IMotDePasseSetting = { id: 456 };
        expectedResult = service.addMotDePasseSettingToCollectionIfMissing([], motDePasseSetting, motDePasseSetting2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(motDePasseSetting);
        expect(expectedResult).toContain(motDePasseSetting2);
      });

      it('should accept null and undefined values', () => {
        const motDePasseSetting: IMotDePasseSetting = { id: 123 };
        expectedResult = service.addMotDePasseSettingToCollectionIfMissing([], null, motDePasseSetting, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(motDePasseSetting);
      });

      it('should return initial array if no MotDePasseSetting is added', () => {
        const motDePasseSettingCollection: IMotDePasseSetting[] = [{ id: 123 }];
        expectedResult = service.addMotDePasseSettingToCollectionIfMissing(motDePasseSettingCollection, undefined, null);
        expect(expectedResult).toEqual(motDePasseSettingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
