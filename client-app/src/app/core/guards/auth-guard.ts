import { Injectable } from '@angular/core';
import {
    Router,
    CanLoad, Route
} from '@angular/router';

import { AuthService } from '@ikubinfo/core/services/auth.service';

@Injectable({
    providedIn: 'root',
})
export class AuthGuard implements CanLoad {
    constructor(private authService: AuthService, private router: Router) { }

    canLoad(route: Route): boolean {
        if (this.authService.isLoggedIn) {
            return true
        }
        else {
            this.router.navigate(['/login']);
            return false;
        }

    }
}


