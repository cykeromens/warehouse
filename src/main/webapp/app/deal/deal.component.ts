import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IDeal } from 'app/shared/model/deal.model';

import { ITEMS_PER_PAGE } from 'app/shared';
import { DealService } from './deal.service';

@Component({
    selector: 'app-deal',
    templateUrl: './deal.component.html'
})
export class DealComponent implements OnInit, OnDestroy {
    deals: IDeal[];
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;

    constructor(
        protected dealService: DealService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
    ) {
        this.deals = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    loadAll() {
        this.dealService
            .query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IDeal[]>) => this.paginateDeals(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    reset() {
        this.page = 0;
        this.deals = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.registerChangeInDeals();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDeal) {
        return item.id;
    }

    registerChangeInDeals() {
        this.eventSubscriber = this.eventManager.subscribe('dealListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateDeals(data: IDeal[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.deals.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
