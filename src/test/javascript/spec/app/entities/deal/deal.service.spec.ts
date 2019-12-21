/* tslint:disable max-line-length */
import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {map, take} from 'rxjs/operators';
import * as moment from 'moment';
import {DATE_FORMAT, DATE_TIME_FORMAT} from 'app/shared/constants/input.constants';
import {DealService} from 'app/entities/deal/deal.service';
import {Deal, IDeal} from 'app/shared/model/deal.model';

describe('Service Tests', () => {
    describe('Deal Service', () => {
        let injector: TestBed;
        let service: DealService;
        let httpMock: HttpTestingController;
        let elemDefault: IDeal;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(DealService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Deal('ID', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, 0, 'AAAAAAA', 'AAAAAAA', currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        time: currentDate.format(DATE_TIME_FORMAT),
                        uploadedOn: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find('123')
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should upload a Deal', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        time: currentDate.format(DATE_TIME_FORMAT),
                        uploadedOn: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        time: currentDate,
                        uploadedOn: currentDate
                    },
                    returnedFromService
                );
                service
                    .upload(null)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({method: 'POST'});
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Deal', async () => {
                const returnedFromService = Object.assign(
                    {
                        tagId: 'BBBBBB',
                        fromIsoCode: 'BBBBBB',
                        toIsoCode: 'BBBBBB',
                        time: currentDate.format(DATE_TIME_FORMAT),
                        amount: 1,
                        source: 'BBBBBB',
                        fileType: 'BBBBBB',
                        uploadedOn: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        time: currentDate,
                        uploadedOn: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
