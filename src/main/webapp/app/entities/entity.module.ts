import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'deal',
                loadChildren: './deal/deal.module#WarehouseDealModule'
            },
            {
                path: 'deal',
                loadChildren: './deal/deal.module#WarehouseDealModule'
            },
            {
                path: 'invalid-deal',
                loadChildren: './invalid-deal/invalid-deal.module#WarehouseInvalidDealModule'
            },
            {
                path: 'summary',
                loadChildren: './summary/summary.module#WarehouseSummaryModule'
            },
            {
                path: 'report',
                loadChildren: './report/report.module#ClusterReportModule'
            }
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WarehouseEntityModule {
}
