import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {WarehouseSharedModule} from 'app/shared';
import {SummaryComponent, SummaryDetailComponent, summaryRoute} from './';

const ENTITY_STATES = [...summaryRoute];

@NgModule({
    imports: [WarehouseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SummaryComponent,
        SummaryDetailComponent,
    ],
    entryComponents: [SummaryComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WarehouseSummaryModule {
}
