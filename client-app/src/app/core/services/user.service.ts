import { Injectable } from '@angular/core';
import { ApiService } from '../utilities/api.service';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
url='user'
constructor(private apiService: ApiService) { }

  getAll(): Observable<Array<User>>{
    return this.apiService.get(this.url);
  }

  getUserById(id: number): Observable<User>{
    return this.apiService.get(this.url+'/'+id);
  }

  edit(user: User):  Observable<void> {
  return this.apiService.put(this.url, user);
}
  delete(): Observable<void> {
    return this.apiService.delete(this.url);
  }
}
