import { Injectable } from '@angular/core';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

constructor(private apiService: ApiService) { }

  register(user: User): Observable<void> {
    this.apiService.post<User>('register', user);
  }
}
