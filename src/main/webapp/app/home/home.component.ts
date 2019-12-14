import { Component, OnInit } from '@angular/core';
import {FileLoader, IFileLoader} from 'app/shared/model/file-loader.model';
import {JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks} from 'ng-jhipster';
import {Observable, Subscription} from 'rxjs';
import {HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {IDeal} from 'app/shared/model/deal.model';
import {DealService} from 'app/deal';
import {ITEMS_PER_PAGE} from 'app/shared';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {

    enableForm: boolean;
    fileLoader: IFileLoader;
    isSaving: boolean;

    deals: IDeal[];
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    fileToUpload: File = null;


    constructor(
        protected dealService: DealService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected dataUtils: JhiDataUtils,

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

    ngOnInit() {
        this.enableForm = true;
        this.isSaving = false;
        this.fileLoader = new FileLoader();
        this.loadAll();
        this.registerChangeInDeals();
    }

    showForm() {
        this.enableForm = false;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.fileToUpload = event.target.files[0];
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        this.enableForm = true;
    }

    save() {
        this.isSaving = true;
        this.subscribeToSaveResponse(this.dealService.create(this.fileToUpload));

    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileLoader>>) {
        result.subscribe((res: HttpResponse<IFileLoader>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
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
