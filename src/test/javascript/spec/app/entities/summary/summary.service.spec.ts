/* tslint:disable max-line-length */
import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {map, take} from 'rxjs/operators';
import * as moment from 'moment';
import {DATE_FORMAT} from 'app/shared/constants/input.constants';
import {SummaryService} from 'app/entities/summary/summary.service';
import {ISummary, Summary} from 'app/shared/model/summary.model';

describe('Service Tests', () => {
    describe('Summary Service', () => {
        let injector: TestBed;
        let service: SummaryService;
        let httpMock: HttpTestingController;
        let elemDefault: ISummary;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SummaryService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Summary('ID', 'AAAAAAA', 0, 0, 0, 0, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        date: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find('123')
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({body: elemDefault}));

                const req = httpMock.expectOne({method: 'GET'});
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Summary', async () => {
                const returnedFromService = Object.assign(
                    {
                        fileName: 'BBBBBB',
                        duration: 1,
                        total: 1,
                        valid: 1,
                        invalid: 1,
                        date: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        date: currentDate
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
                const req = httpMock.expectOne({method: 'GET'});
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
