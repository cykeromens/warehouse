import {Routes} from '@angular/router';

import {HomeComponent} from './';

export const HOME_ROUTE: Routes = [
    {
        path: '',
        component: HomeComponent,
        data: {
            authorities: [],
            pageTitle: 'Welcome, Cluster Data Warehouse!'
        }
    },
    {
        path: 'home',
        redirectTo: '/',
        pathMatch: 'full'

    }
];
