/* tslint:disable max-line-length */
import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {map, take} from 'rxjs/operators';
import {InvalidDealService} from 'app/entities/invalid-deal/invalid-deal.service';
import {IInvalidDeal, InvalidDeal} from 'app/shared/model/invalid-deal.model';

describe('Service Tests', () => {
    describe('InvalidDeal Service', () => {
        let injector: TestBed;
        let service: InvalidDealService;
        let httpMock: HttpTestingController;
        let elemDefault: IInvalidDeal;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(InvalidDealService);
            httpMock = injector.get(HttpTestingController);
            // currentDate = moment();

            elemDefault = new InvalidDeal('AAAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA',
                'AAAAAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                    },
                    elemDefault
                );
                service
                    .find('AAAAAAAA')
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({body: elemDefault}));

                const req = httpMock.expectOne({method: 'GET'});
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of InvalidDeal', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'BBBBBB',
                        fromIsoCode: 'BBBBBB',
                        toIsoCode: 'BBBBBB',
                        time: 'BBBBBBB',
                        amount: 'BBBBBBB',
                        source: 'BBBBBB',
                        extension: 'BBBBBB',
                        uploadedOn: 'BBBBBB',
                        reason: 'BBBBBB',
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
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
