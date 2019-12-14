/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClusterwarehouseTestModule } from '../../test.module';
import { DealDetailComponent } from 'app/deal/deal-detail.component';
import { Deal } from 'app/shared/model/deal.model';

describe('Component Tests', () => {
    describe('Deal Management Detail Component', () => {
        let comp: DealDetailComponent;
        let fixture: ComponentFixture<DealDetailComponent>;
        const route = ({ data: of({ deal: new Deal(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClusterwarehouseTestModule],
                declarations: [DealDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DealDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DealDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.deal).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
