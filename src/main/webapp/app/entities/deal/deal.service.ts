import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IDeal} from 'app/shared/model/deal.model';

type EntityResponseType = HttpResponse<IDeal>;
type EntityArrayResponseType = HttpResponse<IDeal[]>;

@Injectable({ providedIn: 'root' })
export class DealService {
    public resourceUrl = SERVER_API_URL + 'api/deals';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/deals';

    constructor(protected http: HttpClient) {}

    upload(file: File): Observable<EntityResponseType> {

        const formData = new FormData();
        formData.append('file', file, file.name);
        return this.http
            .post<any>(this.resourceUrl, formData, {observe: 'response'});
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IDeal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => res));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDeal[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => res));
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDeal[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
            .pipe(map((res: EntityArrayResponseType) => res));
    }
}
