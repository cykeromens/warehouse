import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {ISummary} from 'app/shared/model/summary.model';

type EntityResponseType = HttpResponse<ISummary>;
type EntityArrayResponseType = HttpResponse<ISummary[]>;

@Injectable({providedIn: 'root'})
export class SummaryService {
    public resourceUrl = SERVER_API_URL + 'api/summaries';

    constructor(protected http: HttpClient) {
    }

    create(summary: ISummary): Observable<EntityResponseType> {
        return this.http.post<ISummary>(this.resourceUrl, summary, {observe: 'response'});
    }

    update(summary: ISummary): Observable<EntityResponseType> {
        return this.http.put<ISummary>(this.resourceUrl, summary, {observe: 'response'});
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISummary>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISummary[]>(this.resourceUrl, {params: options, observe: 'response'});
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }
}
