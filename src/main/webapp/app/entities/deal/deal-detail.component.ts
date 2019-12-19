import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {IDeal} from 'app/shared/model/deal.model';

@Component({
    selector: 'app-deal-detail',
    templateUrl: './deal-detail.component.html'
})
export class DealDetailComponent implements OnInit {
    deal: IDeal;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deal }) => {
            this.deal = deal;
        });
    }

    previousState() {
        window.history.back();
    }
}
