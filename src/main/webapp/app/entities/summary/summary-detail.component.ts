import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {ISummary} from 'app/shared/model/summary.model';

@Component({
    selector: 'app-summary-detail',
    templateUrl: './summary-detail.component.html'
})
export class SummaryDetailComponent implements OnInit {
    summary: ISummary;

    constructor(protected activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({summary}) => {
            this.summary = summary;
        });
    }

    previousState() {
        window.history.back();
    }
}
