import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {ClusterwarehouseSharedModule} from 'app/shared';

import {DealComponent, DealDetailComponent, dealRoute,} from './index';

const ENTITY_STATES = [...dealRoute];

@NgModule({
    imports: [ClusterwarehouseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [DealComponent, DealDetailComponent],
    entryComponents: [DealComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClusterwarehouseDealModule {}
