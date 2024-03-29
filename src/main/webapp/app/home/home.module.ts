import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {WarehouseSharedModule} from 'app/shared';
import {HOME_ROUTE, HomeComponent} from './';

@NgModule({
    imports: [WarehouseSharedModule, RouterModule.forChild(HOME_ROUTE)],
    declarations: [HomeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WarehouseHomeModule {
}
