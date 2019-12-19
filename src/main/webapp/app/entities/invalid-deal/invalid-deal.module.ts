import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {ClusterwarehouseSharedModule} from 'app/shared';
import {InvalidDealComponent, InvalidDealDetailComponent, invalidDealRoute,} from './';

const ENTITY_STATES = [...invalidDealRoute];

@NgModule({
    imports: [ClusterwarehouseSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        InvalidDealComponent,
        InvalidDealDetailComponent,
    ],
    entryComponents: [InvalidDealComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClusterwarehouseInvalidDealModule {
}
