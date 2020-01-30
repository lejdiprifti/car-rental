import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, CanActivateChild } from '@angular/router';

import { AuthService } from '@ikubinfo/core/services/auth.service';
import { RoleEnum } from '@ikubinfo/core/models/role.enum.ts';

@Injectable({ providedIn: 'root' })
export class UserGuard implements CanActivate, CanActivateChild {

    constructor(private authService: AuthService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):boolean {
        return this.authService.role === RoleEnum.USER
    }

    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.authService.role === RoleEnum.USER
    }
}