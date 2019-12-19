import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'deal',
                loadChildren: './deal/deal.module#ClusterwarehouseDealModule'
            },
            {
                path: 'deal',
                loadChildren: './deal/deal.module#ClusterwarehouseDealModule'
            },
            {
                path: 'invalid-deal',
                loadChildren: './invalid-deal/invalid-deal.module#ClusterwarehouseInvalidDealModule'
            },
            {
                path: 'record-count',
                loadChildren: './record-count/record-count.module#ClusterwarehouseRecordCountModule'
            },
            {
                path: 'summary',
                loadChildren: './summary/summary.module#ClusterwarehouseSummaryModule'
            }
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClusterwarehouseEntityModule {
}
