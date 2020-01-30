
import { Injectable } from "@angular/core";
import { tap } from "rxjs/operators";
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpResponse
} from "@angular/common/http";
import { Observable } from 'rxjs';

import { AuthService } from '@ikubinfo/core/services/auth.service';


@Injectable()
export class TokenInterceptor implements HttpInterceptor {
    constructor(private authService: AuthService) { }
    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        if (!(request.url.endsWith('login') || request.url.endsWith('register'))) {
            request = request.clone({
                headers: request.headers.set("Authorization", "Bearer "+ sessionStorage.getItem("token"))
            });
        }

        return next.handle(request).pipe(
            tap(
                event => {
                    if (event instanceof HttpResponse) {
                        console.log("api call success :", event);
                    }
                },
                error => {
                    if (event instanceof HttpResponse) {
                        console.log("api call error :", event);
                    }
                }
            )
        );
    }
}