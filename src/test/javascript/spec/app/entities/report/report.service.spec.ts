/* tslint:disable max-line-length */
import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {map, take} from 'rxjs/operators';
import * as moment from 'moment';
import {DATE_TIME_FORMAT} from 'app/shared/constants/input.constants';
import {ReportService} from 'app/entities/report/report.service';
import {IReport, Report} from 'app/shared/model/report.model';

describe('Service Tests', () => {
    describe('Report Service', () => {
        let injector: TestBed;
        let service: ReportService;
        let httpMock: HttpTestingController;
        let elemDefault: IReport;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ReportService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Report('ID', 'AAAAAAA', 'AAAAAAA', 0, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        lastUpdated: currentDate.format(DATE_TIME_FORMAT)
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

            it('should return a list of Report', async () => {
                const returnedFromService = Object.assign(
                    {
                        fromIsoCode: 'BBBBBB',
                        toIsoCode: 'BBBBBB',
                        total: 1,
                        lastUpdated: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        lastUpdated: currentDate
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
