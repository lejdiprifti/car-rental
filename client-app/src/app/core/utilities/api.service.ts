
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { BASE_URL } from '@env/environment';

@Injectable()
export class ApiService {

  private options = { headers: new HttpHeaders().set('Content-Type', 'application/json') };

  constructor(private httpClient: HttpClient) {

  }

  public get<Model>(path: string, params: HttpParams = new HttpParams()): Observable<Model> {
    return this.httpClient.get<Model>(BASE_URL + path, { params });
  }

  public getWithBody<Model>(path: string, body: object = {}): Observable<Model> {
    return this.httpClient.post<Model>(BASE_URL + path, JSON.stringify(body), this.options);
  }

  public put<Model>(path: string, body: object = {}): Observable<Model> {
    return this.httpClient
      .put<Model>(BASE_URL + path, JSON.stringify(body), this.options);
  }

  public post<Model>(path: string, body: object = {}): Observable<Model> {
    return this.httpClient
      .post<Model>(BASE_URL + path, JSON.stringify(body), this.options);
  }

  public delete(path: string): Observable<void> {
    return this.httpClient.delete<void>(BASE_URL + path);
  }



}