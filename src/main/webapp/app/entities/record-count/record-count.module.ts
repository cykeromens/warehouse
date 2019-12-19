import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {ClusterwarehouseSharedModule} from 'app/shared';
import {RecordCountComponent, recordCountRoute,} from './';

const ENTITY_STATES = [...recordCountRoute];

@NgModule({
    imports: [ClusterwarehouseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RecordCountComponent,
    ],
    entryComponents: [RecordCountComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClusterwarehouseRecordCountModule {
}
