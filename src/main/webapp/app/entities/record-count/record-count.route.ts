import {Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {RecordCountComponent} from './record-count.component';

export const recordCountRoute: Routes = [
    {
        path: '',
        component: RecordCountComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'RecordCounts'
        },
    },
];
