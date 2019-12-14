import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit {
    isNavbarCollapsed: boolean;
    version: string;

    constructor(
        private router: Router
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    ngOnInit() {
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return true;
    }

    login() {
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }
}
