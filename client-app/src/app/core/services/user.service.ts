import { Injectable } from '@angular/core';
import { ApiService } from '../utilities/api.service';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { UserPage } from '../models/user-page';
import { HttpParams } from '@angular/common/http';
import { UserFilter } from '../models/user-filter';

@Injectable({
  providedIn: 'root'
})
export class UserService {
private url: string ='user';
private userFilter: UserFilter;
constructor(private apiService: ApiService) { }

  getAll(startIndex: number, pageSize: number, name?: string): Observable<UserPage>{
    this.userFilter = {};
    this.userFilter.startIndex = startIndex;
    this.userFilter.pageSize = pageSize;
    if (name){
      this.userFilter.name = name;
    }
    return this.apiService.getWithBody(this.url, this.userFilter);
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
