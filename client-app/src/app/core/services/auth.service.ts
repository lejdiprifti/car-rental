

import { Injectable } from '@angular/core';


import { Observable, of, throwError, Subject } from 'rxjs';
import { isNullOrUndefined } from 'util';

import { User } from '@ikubinfo/core/models/user';
import { Role } from '@ikubinfo/core/models/role.enum';
import { Login } from '@ikubinfo/core/models/login';

const mockUsers: Array<User> = [{
    username: 'rmusaj',
    email: 'romina.musaj@ikubinfo.al',
    id: 1,
    role: Role.ADMIN
},
{
    username: 'gcota',
    email: 'guido.cota@ikubinfo.al',
    id: 1,
    role: Role.USER
}];

@Injectable()
export class AuthService {

    user: User;
    onUserChanged: Subject<User>;

    constructor() {
        this.onUserChanged = new Subject<User>();
        this.loadData();
    }

    //TO-DO: Mock Login => Need to be implemented with the real api calls

    login(loginContext: Login): Observable<any> {
        if (
            mockUsers.find(user => user.username === loginContext.username)) {
            return of(mockUsers.find(user => user.username === loginContext.username));
        }
        return throwError('Invalid username or password');
    }


    logout(): void {
        this.setData(null);
    }

    getToken() {
        return this.getToken;
    }

    setData(user: User) {
        console.debug(
            user ? "Setting current user info. : Clearing current user info." : ""
        );
        this.user = user;
        if (user) {
            sessionStorage.setItem("userData", JSON.stringify(user));
        } else {
            sessionStorage.removeItem("userData");
        }
        this.onUserChanged.next(user);
    }

    get isLoggedIn(): boolean {
        this.loadData();
        return (this.user && !isNullOrUndefined(this.user.username));
    }

    private loadData() {

        if (sessionStorage.getItem("userData")) {
            this.user = JSON.parse(sessionStorage.getItem("userData"));
        }
        else {
            this.user = null;
        }

    }

    get role(): Role {
        if (this.isLoggedIn) {
            return this.user.role;
        }
        return null;
    }


}