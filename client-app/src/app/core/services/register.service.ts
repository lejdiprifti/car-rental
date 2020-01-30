import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { ApiService } from '../utilities/api.service';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
registerUser: User;
onUserChanged: Subject<User>;
constructor(private apiService: ApiService) { }
 
  register(user: User): Observable<void> {
    return this.apiService.post<void>('register', user);
  }
}
