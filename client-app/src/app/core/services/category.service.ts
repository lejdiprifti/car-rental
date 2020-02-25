import { Injectable } from '@angular/core';
import { ApiService } from '../utilities/api.service';
import { Observable } from 'rxjs';
import { Category } from '../models/category';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CategoryPage } from '../models/category-page';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
url = 'category'
private options = { headers: new HttpHeaders().set('Content-Type', 'multipart/form-data') };

constructor(private apiService: ApiService, private httpClient: HttpClient) { }

public getAll(startIndex: number, pageSize: number): Observable<CategoryPage>{
  return this.apiService.get(this.url+'?startIndex='+startIndex+'&pageSize='+pageSize);
}

public getAllCategories(): Observable<CategoryPage>{
  return this.apiService.get(this.url+'/all');
}

public getById(id: number): Observable<Category>{
  return this.apiService.get(this.url + '/'+id);
}

public save(formData: FormData): Observable<any>{
  return this.httpClient.post('http://localhost:8080/category', formData);
}

public edit(formData: FormData, id: number): Observable<any>{
  return this.httpClient.put('http://localhost:8080/category/' + id, formData);
}

public delete(id: number): Observable<void>{
  return this.apiService.delete(this.url+'/'+id);
}

}
