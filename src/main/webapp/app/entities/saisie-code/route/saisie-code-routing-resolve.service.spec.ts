import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISaisieCode, SaisieCode } from '../saisie-code.model';
import { SaisieCodeService } from '../service/saisie-code.service';

import { SaisieCodeRoutingResolveService } from './saisie-code-routing-resolve.service';

describe('SaisieCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SaisieCodeRoutingResolveService;
  let service: SaisieCodeService;
  let resultSaisieCode: ISaisieCode | undefined;

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
    routingResolveService = TestBed.inject(SaisieCodeRoutingResolveService);
    service = TestBed.inject(SaisieCodeService);
    resultSaisieCode = undefined;
  });

  describe('resolve', () => {
    it('should return ISaisieCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSaisieCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSaisieCode).toEqual({ id: 123 });
    });

    it('should return new ISaisieCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSaisieCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSaisieCode).toEqual(new SaisieCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SaisieCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSaisieCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSaisieCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
