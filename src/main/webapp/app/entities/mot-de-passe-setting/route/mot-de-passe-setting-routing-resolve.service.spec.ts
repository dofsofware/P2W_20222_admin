import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMotDePasseSetting, MotDePasseSetting } from '../mot-de-passe-setting.model';
import { MotDePasseSettingService } from '../service/mot-de-passe-setting.service';

import { MotDePasseSettingRoutingResolveService } from './mot-de-passe-setting-routing-resolve.service';

describe('MotDePasseSetting routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MotDePasseSettingRoutingResolveService;
  let service: MotDePasseSettingService;
  let resultMotDePasseSetting: IMotDePasseSetting | undefined;

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
    routingResolveService = TestBed.inject(MotDePasseSettingRoutingResolveService);
    service = TestBed.inject(MotDePasseSettingService);
    resultMotDePasseSetting = undefined;
  });

  describe('resolve', () => {
    it('should return IMotDePasseSetting returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMotDePasseSetting = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMotDePasseSetting).toEqual({ id: 123 });
    });

    it('should return new IMotDePasseSetting if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMotDePasseSetting = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMotDePasseSetting).toEqual(new MotDePasseSetting());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MotDePasseSetting })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMotDePasseSetting = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMotDePasseSetting).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
