import {Component, OnInit} from '@angular/core';
import {ActivatedRouteSnapshot, NavigationEnd, NavigationError, Router} from '@angular/router';

import {Title} from '@angular/platform-browser';

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html'
})
export class AppMainComponent implements OnInit {
    constructor(private titleService: Title, private router: Router) {}

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'warehouseApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnInit() {
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.titleService.setTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
            if (event instanceof NavigationError && event.error.status === 404) {
                this.router.navigate(['/404']);
            }
        });
    }
}
