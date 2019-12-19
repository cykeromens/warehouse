import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {Observable, of} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {ISummary, Summary} from 'app/shared/model/summary.model';
import {SummaryService} from './summary.service';
import {SummaryComponent} from './summary.component';
import {SummaryDetailComponent} from './summary-detail.component';

@Injectable({providedIn: 'root'})
export class SummaryResolve implements Resolve<ISummary> {
    constructor(private service: SummaryService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISummary> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Summary>) => response.ok),
                map((summary: HttpResponse<Summary>) => summary.body)
            );
        }
        return of(new Summary());
    }
}

export const summaryRoute: Routes = [
    {
        path: '',
        component: SummaryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Summaries'
        },
    },
    {
        path: ':id/view',
        component: SummaryDetailComponent,
        resolve: {
            summary: SummaryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Summaries'
        },
    }
];
