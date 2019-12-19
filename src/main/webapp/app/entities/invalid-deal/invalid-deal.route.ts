import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {Observable, of} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {IInvalidDeal, InvalidDeal} from 'app/shared/model/invalid-deal.model';
import {InvalidDealService} from './invalid-deal.service';
import {InvalidDealComponent} from './invalid-deal.component';
import {InvalidDealDetailComponent} from './invalid-deal-detail.component';

@Injectable({providedIn: 'root'})
export class InvalidDealResolve implements Resolve<IInvalidDeal> {
    constructor(private service: InvalidDealService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInvalidDeal> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<InvalidDeal>) => response.ok),
                map((invalidDeal: HttpResponse<InvalidDeal>) => invalidDeal.body)
            );
        }
        return of(new InvalidDeal());
    }
}

export const invalidDealRoute: Routes = [
    {
        path: '',
        component: InvalidDealComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'InvalidDeals'
        },
    },
    {
        path: ':id/view',
        component: InvalidDealDetailComponent,
        resolve: {
            invalidDeal: InvalidDealResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InvalidDeals'
        },
    }
];
