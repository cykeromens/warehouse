import {DatePipe} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {ElementRef, NgModule, Renderer} from '@angular/core';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {JhiAlertService, JhiDataUtils, JhiDateUtils, JhiEventManager, JhiParseLinks} from 'ng-jhipster';

import {MockActivatedRoute, MockRouter} from './helpers/mock-route.service';
import {MockEventManager} from './helpers/mock-event-manager.service';

@NgModule({
    providers: [
        DatePipe,
        JhiDataUtils,
        JhiDateUtils,
        JhiParseLinks,
        {
            provide: JhiEventManager,
            useClass: MockEventManager
        },
        {
            provide: ActivatedRoute,
            useValue: new MockActivatedRoute({id: '123'})
        },
        {
            provide: Router,
            useClass: MockRouter
        },
        {
            provide: ElementRef,
            useValue: null
        },
        {
            provide: Renderer,
            useValue: null
        },
        {
            provide: JhiAlertService,
            useValue: null
        }
    ],
    imports: [HttpClientTestingModule]
})
export class ClusterTestModule {
}
