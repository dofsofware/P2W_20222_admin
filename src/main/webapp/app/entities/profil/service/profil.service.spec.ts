import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProfil, Profil } from '../profil.model';

import { ProfilService } from './profil.service';

describe('Profil Service', () => {
  let service: ProfilService;
  let httpMock: HttpTestingController;
  let elemDefault: IProfil;
  let expectedResult: IProfil | IProfil[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProfilService);
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

    it('should create a Profil', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Profil()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Profil', () => {
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

    it('should partial update a Profil', () => {
      const patchObject = Object.assign({}, new Profil());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Profil', () => {
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

    it('should delete a Profil', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProfilToCollectionIfMissing', () => {
      it('should add a Profil to an empty array', () => {
        const profil: IProfil = { id: 123 };
        expectedResult = service.addProfilToCollectionIfMissing([], profil);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(profil);
      });

      it('should not add a Profil to an array that contains it', () => {
        const profil: IProfil = { id: 123 };
        const profilCollection: IProfil[] = [
          {
            ...profil,
          },
          { id: 456 },
        ];
        expectedResult = service.addProfilToCollectionIfMissing(profilCollection, profil);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Profil to an array that doesn't contain it", () => {
        const profil: IProfil = { id: 123 };
        const profilCollection: IProfil[] = [{ id: 456 }];
        expectedResult = service.addProfilToCollectionIfMissing(profilCollection, profil);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(profil);
      });

      it('should add only unique Profil to an array', () => {
        const profilArray: IProfil[] = [{ id: 123 }, { id: 456 }, { id: 35263 }];
        const profilCollection: IProfil[] = [{ id: 123 }];
        expectedResult = service.addProfilToCollectionIfMissing(profilCollection, ...profilArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const profil: IProfil = { id: 123 };
        const profil2: IProfil = { id: 456 };
        expectedResult = service.addProfilToCollectionIfMissing([], profil, profil2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(profil);
        expect(expectedResult).toContain(profil2);
      });

      it('should accept null and undefined values', () => {
        const profil: IProfil = { id: 123 };
        expectedResult = service.addProfilToCollectionIfMissing([], null, profil, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(profil);
      });

      it('should return initial array if no Profil is added', () => {
        const profilCollection: IProfil[] = [{ id: 123 }];
        expectedResult = service.addProfilToCollectionIfMissing(profilCollection, undefined, null);
        expect(expectedResult).toEqual(profilCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
