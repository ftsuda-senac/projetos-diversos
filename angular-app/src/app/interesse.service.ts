import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { PagedResult } from './pagedResult';
import { Interesse } from './interesse';

@Injectable({
  providedIn: 'root'
})
export class InteresseService {

  private apiBaseUrl = 'http://localhost:8080/rest/interesses';

  constructor(
    private http: HttpClient
  ) {

  }

  getInteresses() : Observable<Interesse[]> {
    return this.http.get<Interesse[]>(this.apiBaseUrl);
  }

}
