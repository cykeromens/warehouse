import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {Observable, of} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {Deal, IDeal} from 'app/shared/model/deal.model';
import {DealService} from './deal.service';
import {DealComponent} from './deal.component';
import {DealDetailComponent} from './deal-detail.component';
import {JhiResolvePagingParams} from 'ng-jhipster';

@Injectable({ providedIn: 'root' })
export class DealResolve implements Resolve<IDeal> {
    constructor(private service: DealService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDeal> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Deal>) => response.ok),
                map((deal: HttpResponse<Deal>) => deal.body)
            );
        }
        return of(new Deal());
    }
}

export const dealRoute: Routes = [
    {
        path: '',
        component: DealComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'fromIsoCode,asc',
            pageTitle: 'Deals'
        }
    },
    {
        path: ':fileName',
        component: DealComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'fromIsoCode,asc',
            pageTitle: 'Deals'
        }
    },
    {
        path: ':id/view',
        component: DealDetailComponent,
        resolve: {
            deal: DealResolve
        },
        data: {
            authorities: [],
            pageTitle: 'Deals'
        }
    },
];
