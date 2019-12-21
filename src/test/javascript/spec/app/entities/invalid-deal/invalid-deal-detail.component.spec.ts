/* tslint:disable max-line-length */
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';

import {ClusterTestModule} from '../../../test.module';
import {InvalidDealDetailComponent} from 'app/entities/invalid-deal/invalid-deal-detail.component';
import {InvalidDeal} from 'app/shared/model/invalid-deal.model';

describe('Component Tests', () => {
    describe('InvalidDeal Management Detail Component', () => {
        let comp: InvalidDealDetailComponent;
        let fixture: ComponentFixture<InvalidDealDetailComponent>;
        const route = ({data: of({invalidDeal: new InvalidDeal('123')})} as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClusterTestModule],
                declarations: [InvalidDealDetailComponent],
                providers: [{provide: ActivatedRoute, useValue: route}]
            })
                .overrideTemplate(InvalidDealDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InvalidDealDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.invalidDeal).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });
});
