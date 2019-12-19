import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as moment from 'moment';
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IInvalidDeal} from 'app/shared/model/invalid-deal.model';

type EntityResponseType = HttpResponse<IInvalidDeal>;
type EntityArrayResponseType = HttpResponse<IInvalidDeal[]>;

@Injectable({providedIn: 'root'})
export class InvalidDealService {
    public resourceUrl = SERVER_API_URL + 'api/invalid-deals';

    constructor(protected http: HttpClient) {
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IInvalidDeal>(`${this.resourceUrl}/${id}`, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IInvalidDeal[]>(this.resourceUrl, {params: options, observe: 'response'})
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.time = res.body.time != null ? moment(res.body.time) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((invalidDeal: IInvalidDeal) => {
                invalidDeal.time = invalidDeal.time != null ? moment(invalidDeal.time) : null;
            });
        }
        return res;
    }
}
