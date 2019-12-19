import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {IInvalidDeal} from 'app/shared/model/invalid-deal.model';

@Component({
    selector: 'app-invalid-deal-detail',
    templateUrl: './invalid-deal-detail.component.html'
})
export class InvalidDealDetailComponent implements OnInit {
    invalidDeal: IInvalidDeal;

    constructor(protected activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({invalidDeal}) => {
            this.invalidDeal = invalidDeal;
        });
    }

    previousState() {
        window.history.back();
    }
}
