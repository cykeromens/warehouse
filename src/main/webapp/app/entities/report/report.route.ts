import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {Observable, of} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {IReport, Report} from 'app/shared/model/report.model';
import {ReportService} from './report.service';
import {ReportComponent} from './report.component';
import {ReportDetailComponent} from './report-detail.component';

@Injectable({providedIn: 'root'})
export class ReportResolve implements Resolve<IReport> {
    constructor(private service: ReportService) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IReport> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Report>) => response.ok),
                map((report: HttpResponse<Report>) => report.body)
            );
        }
        return of(new Report());
    }
}

export const reportRoute: Routes = [
    {
        path: '',
        component: ReportComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            defaultSort: 'id,asc',
            pageTitle: 'Reports'
        }
    },
    {
        path: ':id/view',
        component: ReportDetailComponent,
        resolve: {
            report: ReportResolve
        },
        data: {
            pageTitle: 'Reports'
        }
    }
];
