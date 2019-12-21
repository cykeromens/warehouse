import {NgModule} from '@angular/core';

import {JhiAlertComponent, JhiAlertErrorComponent, WarehouseSharedLibsModule} from './';

@NgModule({
    imports: [WarehouseSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [WarehouseSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class WarehouseSharedCommonModule {
}
