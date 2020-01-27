import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, CanActivateChild } from '@angular/router';

import { AuthService } from '@ikubinfo/core/services/auth.service';
import { Role } from '@ikubinfo/core/models/role.enum';

@Injectable({ providedIn: 'root' })
export class UserGuard implements CanActivate, CanActivateChild {

    constructor(private authService: AuthService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):boolean {
        return this.authService.role === Role.USER
    }

    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.authService.role === Role.USER
    }
}