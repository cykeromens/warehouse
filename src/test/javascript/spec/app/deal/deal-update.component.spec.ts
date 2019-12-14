// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
// import { HttpResponse } from '@angular/common/http';
// import { Observable, of } from 'rxjs';
//
// import { ClusterwarehouseTestModule } from '../../test.module';
// import { DealUpdateComponent } from 'app/deal/deal-update.component';
// import { DealService } from 'app/deal/deal.service';
// import { Deal } from 'app/shared/model/deal.model';
//
// describe('Component Tests', () => {
//     describe('Deal Management Update Component', () => {
//         let comp: DealUpdateComponent;
//         let fixture: ComponentFixture<DealUpdateComponent>;
//         let service: DealService;
//
//         beforeEach(() => {
//             TestBed.configureTestingModule({
//                 imports: [ClusterwarehouseTestModule],
//                 declarations: [DealUpdateComponent]
//             })
//                 .overrideTemplate(DealUpdateComponent, '')
//                 .compileComponents();
//
//             fixture = TestBed.createComponent(DealUpdateComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(DealService);
//         });
//
//         describe('save', () => {
//             it('Should call update service on save for existing entity', fakeAsync(() => {
//                 // GIVEN
//                 const entity = new Deal(123);
//                 spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
//                 comp.deal = entity;
//                 // WHEN
//                 comp.save();
//                 tick(); // simulate async
//
//                 // THEN
//                 expect(service.update).toHaveBeenCalledWith(entity);
//                 expect(comp.isSaving).toEqual(false);
//             }));
//
//             it('Should call create service on save for new entity', fakeAsync(() => {
//                 // GIVEN
//                 const entity = new Deal();
//                 spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
//                 comp.deal = entity;
//                 // WHEN
//                 comp.save();
//                 tick(); // simulate async
//
//                 // THEN
//                 expect(service.create).toHaveBeenCalledWith(entity);
//                 expect(comp.isSaving).toEqual(false);
//             }));
//         });
//     });
// });
