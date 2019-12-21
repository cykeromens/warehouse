import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {NgbDateAdapter} from '@ng-bootstrap/ng-bootstrap';

import {NgbDateMomentAdapter} from './util/datepicker-adapter';
import {WarehouseSharedCommonModule, WarehouseSharedLibsModule} from './';

@NgModule({
    imports: [WarehouseSharedLibsModule, WarehouseSharedCommonModule],
    declarations: [],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [],
    exports: [WarehouseSharedCommonModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WarehouseSharedModule {
    static forRoot() {
        return {
            ngModule: WarehouseSharedModule
        };
    }
}
