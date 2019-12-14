import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { ClusterwarehouseSharedLibsModule, ClusterwarehouseSharedCommonModule } from './';

@NgModule({
    imports: [ClusterwarehouseSharedLibsModule, ClusterwarehouseSharedCommonModule],
    declarations: [],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [],
    exports: [ClusterwarehouseSharedCommonModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClusterwarehouseSharedModule {
    static forRoot() {
        return {
            ngModule: ClusterwarehouseSharedModule
        };
    }
}
