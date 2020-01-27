import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from '@angular/router';

import { AuthService } from '@ikubinfo/core/services/auth.service';
import { RoleEnum } from '@ikubinfo/core/models/role.enum';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
    constructor(private authService: AuthService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):boolean {
        return this.authService.role === RoleEnum.ADMIN;
    }
    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.authService.role === RoleEnum.ADMIN
    }
}