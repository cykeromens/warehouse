import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as moment from 'moment';
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IReport} from 'app/shared/model/report.model';

type EntityResponseType = HttpResponse<IReport>;
type EntityArrayResponseType = HttpResponse<IReport[]>;

@Injectable({providedIn: 'root'})
export class ReportService {
    public resourceUrl = SERVER_API_URL + 'api/reports';

    constructor(protected http: HttpClient) {
    }

    create(report: IReport): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(report);
        return this.http
            .post<IReport>(this.resourceUrl, copy, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(report: IReport): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(report);
        return this.http
            .put<IReport>(this.resourceUrl, copy, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IReport>(`${this.resourceUrl}/${id}`, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReport[]>(this.resourceUrl, {params: options, observe: 'response'})
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }

    protected convertDateFromClient(report: IReport): IReport {
        const copy: IReport = Object.assign({}, report, {
            lastUpdated: report.lastUpdated != null && report.lastUpdated.isValid() ? report.lastUpdated.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.lastUpdated = res.body.lastUpdated != null ? moment(res.body.lastUpdated) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((report: IReport) => {
                report.lastUpdated = report.lastUpdated != null ? moment(report.lastUpdated) : null;
            });
        }
        return res;
    }
}
