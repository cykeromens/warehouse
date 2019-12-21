import './vendor.ts';

import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgbDatepickerConfig} from '@ng-bootstrap/ng-bootstrap';
import {Ng2Webstorage} from 'ngx-webstorage';
import {NgJhipsterModule} from 'ng-jhipster';

import {ErrorHandlerInterceptor} from './blocks/interceptor/errorhandler.interceptor';
import {NotificationInterceptor} from './blocks/interceptor/notification.interceptor';
import {WarehouseSharedModule} from 'app/shared';
import {WarehouseAppRoutingModule} from './app-routing.module';
import * as moment from 'moment';
import {AppMainComponent, ErrorComponent, FooterComponent, NavbarComponent} from './layouts';
import {WarehouseHomeModule} from 'app/home';
import {WarehouseEntityModule} from 'app/entities/entity.module';

@NgModule({
    imports: [
        BrowserModule,
        HttpClientModule,
        Ng2Webstorage.forRoot({ prefix: 'app', separator: '-' }),
        NgJhipsterModule.forRoot({
            alertAsToast: false,
            alertTimeout: 5000
        }),
        WarehouseSharedModule.forRoot(),
        WarehouseHomeModule,
        WarehouseEntityModule,
        WarehouseAppRoutingModule
    ],
    declarations: [AppMainComponent, NavbarComponent, ErrorComponent, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [AppMainComponent]
})
export class WarehouseAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
