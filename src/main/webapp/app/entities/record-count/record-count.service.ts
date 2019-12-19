import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IRecordCount} from 'app/shared/model/record-count.model';

type EntityResponseType = HttpResponse<IRecordCount>;
type EntityArrayResponseType = HttpResponse<IRecordCount[]>;

@Injectable({providedIn: 'root'})
export class RecordCountService {
    public resourceUrl = SERVER_API_URL + 'api/record-counts';

    constructor(protected http: HttpClient) {
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRecordCount>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRecordCount[]>(this.resourceUrl, {params: options, observe: 'response'});
    }
}
