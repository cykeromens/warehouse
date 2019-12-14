import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import {IFileLoader} from "app/shared/model/file-loader.model";
import {JhiDataUtils} from "ng-jhipster";

@Component({
    selector: 'app-deal-update',
    templateUrl: './deal-update.component.html'
})
export class DealUpdateComponent implements OnInit {
    fileLoader: IFileLoader;
    isSaving: boolean;

    constructor(
        protected dataUtils: JhiDataUtils,
        // protected fileLoaderService: FileLoaderService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fileLoader }) => {
            this.fileLoader = fileLoader;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.fileLoader.id !== undefined) {
            // this.subscribeToSaveResponse(this.fileLoaderService.update(this.fileLoader));
        } else {
            // this.subscribeToSaveResponse(this.fileLoaderService.create(this.fileLoader));
        }
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
}
