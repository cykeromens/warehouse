/* tslint:disable max-line-length */
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';

import {ClusterTestModule} from '../../../test.module';
import {SummaryDetailComponent} from 'app/entities/summary/summary-detail.component';
import {Summary} from 'app/shared/model/summary.model';

describe('Component Tests', () => {
    describe('Summary Management Detail Component', () => {
        let comp: SummaryDetailComponent;
        let fixture: ComponentFixture<SummaryDetailComponent>;
        const route = ({data: of({summary: new Summary('123')})} as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClusterTestModule],
                declarations: [SummaryDetailComponent],
                providers: [{provide: ActivatedRoute, useValue: route}]
            })
                .overrideTemplate(SummaryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SummaryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.summary).toEqual(jasmine.objectContaining({id: '123'}));
            });
        });
    });
});
