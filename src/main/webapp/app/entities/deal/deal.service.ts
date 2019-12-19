import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as moment from 'moment';
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IDeal} from 'app/shared/model/deal.model';
import {IFileLoader} from 'app/shared/model/file-loader.model';

type EntityResponseType = HttpResponse<IDeal>;
type EntityArrayResponseType = HttpResponse<IDeal[]>;

@Injectable({ providedIn: 'root' })
export class DealService {
    public resourceUrl = SERVER_API_URL + 'api/deals';
    public resourceDistinctUrl = SERVER_API_URL + 'api/deals/source';

    constructor(protected http: HttpClient) {}

    create(fileLoader: File): Observable<EntityResponseType> {

        const formData = new FormData();
        formData.append('file', fileLoader, fileLoader.name);
        return this.http
            .post<any>(this.resourceUrl, formData, {observe: 'response'});
    }

    update(fileLoader: IFileLoader): Observable<EntityResponseType> {
        return this.http.put<IFileLoader>(this.resourceUrl, fileLoader, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDeal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDeal[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    distinct(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDeal[]>(this.resourceDistinctUrl, {params: options, observe: 'response'})
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(deal: IDeal): IDeal {
        const copy: IDeal = Object.assign({}, deal, {
            time: deal.time != null && deal.time.isValid() ? deal.time.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.time = res.body.time != null ? moment(res.body.time) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((deal: IDeal) => {
                deal.time = deal.time != null ? moment(deal.time) : null;
            });
        }
        return res;
    }
}
