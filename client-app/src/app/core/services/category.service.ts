import { Injectable } from '@angular/core';
import { ApiService } from '../utilities/api.service';
import { Observable } from 'rxjs';
import { Category } from '../models/category';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
url = 'category'
private options = { headers: new HttpHeaders().set('Content-Type', 'multipart/form-data') };

constructor(private apiService: ApiService, private httpClient: HttpClient) { }

public getAll(): Observable<Array<Category>>{
  return this.apiService.get(this.url);
}

public getById(id: number): Observable<Category>{
  return this.apiService.get(this.url + '/'+id);
}

public save(formData: FormData): Observable<any>{
  return this.httpClient.post('http://localhost:8080/category', formData);
}

public edit(category: Category, id: number): Observable<void>{
  return this.apiService.put(this.url+'/'+id, category);
}

public delete(id: number): Observable<void>{
  return this.apiService.delete(this.url+'/'+id);
}

}
