import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDeal } from 'app/shared/model/deal.model';
import { DealService } from './deal.service';

@Component({
    selector: 'app-deal-delete-dialog',
    templateUrl: './deal-delete-dialog.component.html'
})
export class DealDeleteDialogComponent {
    deal: IDeal;

    constructor(protected dealService: DealService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dealService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'dealListModification',
                content: 'Deleted an deal'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'app-deal-delete-popup',
    template: ''
})
export class DealDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deal }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DealDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.deal = deal;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/deal', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/deal', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
