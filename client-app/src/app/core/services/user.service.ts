import { Injectable } from '@angular/core';
import { ApiService } from '../utilities/api.service';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { UserPage } from '../models/user-page';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
url='user'
constructor(private apiService: ApiService) { }

  getAll(startIndex: number, pageSize: number, name?: string): Observable<UserPage>{
    let params = new HttpParams();
    if (name){
      params = params.set('name', name);
    }
    return this.apiService.get(this.url+'?startIndex='+startIndex+'&pageSize='+pageSize, params);
  }

  getUserById(id: number): Observable<User>{
    return this.apiService.get(this.url+'/'+id);
  }

  editPassword(user: User):  Observable<void> {
  return this.apiService.put(this.url + '/update/password', user);
  }

  editProfile(user: User): Observable<void> {
    return this.apiService.put(this.url + '/update/profile', user);
  }
  delete(): Observable<void> {
    return this.apiService.delete(this.url);
  }
}
