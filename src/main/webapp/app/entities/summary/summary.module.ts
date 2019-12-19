import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {ClusterwarehouseSharedModule} from 'app/shared';
import {SummaryComponent, SummaryDetailComponent, summaryRoute,} from './';

const ENTITY_STATES = [...summaryRoute];

@NgModule({
    imports: [ClusterwarehouseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SummaryComponent,
        SummaryDetailComponent,
    ],
    entryComponents: [SummaryComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClusterwarehouseSummaryModule {
}
