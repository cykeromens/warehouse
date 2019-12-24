import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {ReportComponent, ReportDetailComponent, reportRoute} from './';
import {WarehouseSharedModule} from 'app/shared';

const ENTITY_STATES = [...reportRoute];

@NgModule({
    imports: [WarehouseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ReportComponent, ReportDetailComponent],
    entryComponents: [ReportComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClusterReportModule {
}
