import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IInvalidDeal} from 'app/shared/model/invalid-deal.model';

type EntityResponseType = HttpResponse<IInvalidDeal>;
type EntityArrayResponseType = HttpResponse<IInvalidDeal[]>;

@Injectable({providedIn: 'root'})
export class InvalidDealService {
    public resourceUrl = SERVER_API_URL + 'api/invalid-deals';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/invalid_deals';

    constructor(protected http: HttpClient) {
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IInvalidDeal>(`${this.resourceUrl}/${id}`, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => res));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IInvalidDeal[]>(this.resourceUrl, {params: options, observe: 'response'})
            .pipe(map((res: EntityArrayResponseType) => res));
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IInvalidDeal[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
            .pipe(map((res: EntityArrayResponseType) => res));
    }
}
