import { NgModule } from '@angular/core';

import { ClusterwarehouseSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ClusterwarehouseSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ClusterwarehouseSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ClusterwarehouseSharedCommonModule {}
