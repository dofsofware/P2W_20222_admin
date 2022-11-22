import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInfosAbonne, InfosAbonne } from '../infos-abonne.model';
import { InfosAbonneService } from '../service/infos-abonne.service';

import { InfosAbonneRoutingResolveService } from './infos-abonne-routing-resolve.service';

describe('InfosAbonne routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InfosAbonneRoutingResolveService;
  let service: InfosAbonneService;
  let resultInfosAbonne: IInfosAbonne | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(InfosAbonneRoutingResolveService);
    service = TestBed.inject(InfosAbonneService);
    resultInfosAbonne = undefined;
  });

  describe('resolve', () => {
    it('should return IInfosAbonne returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfosAbonne = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfosAbonne).toEqual({ id: 123 });
    });

    it('should return new IInfosAbonne if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfosAbonne = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInfosAbonne).toEqual(new InfosAbonne());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InfosAbonne })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfosAbonne = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfosAbonne).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
