import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { ClusterwarehouseSharedModule } from 'app/shared';
import { ClusterwarehouseAppRoutingModule } from './app-routing.module';
import * as moment from 'moment';
import { AppMainComponent, NavbarComponent, FooterComponent, ErrorComponent } from './layouts';
import {ClusterwarehouseDealModule} from 'app/deal/deal.module';
import {ClusterwarehouseHomeModule} from "app/home";

@NgModule({
    imports: [
        BrowserModule,
        HttpClientModule,
        Ng2Webstorage.forRoot({ prefix: 'app', separator: '-' }),
        NgJhipsterModule.forRoot({
            alertAsToast: false,
            alertTimeout: 5000
        }),
        ClusterwarehouseSharedModule.forRoot(),
        ClusterwarehouseHomeModule,
        ClusterwarehouseDealModule,
        ClusterwarehouseAppRoutingModule
    ],
    declarations: [AppMainComponent, NavbarComponent, ErrorComponent, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
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
export class ClusterwarehouseAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
