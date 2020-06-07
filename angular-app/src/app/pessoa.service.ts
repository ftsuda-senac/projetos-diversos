import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { PagedResult } from './pagedResult';
import { Pessoa } from './pessoa';

@Injectable({
  providedIn: 'root'
})
export class PessoaService {

  private apiBaseUrl = 'http://localhost:8080/rest/pessoas';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient
  ) {

  }

  getPagedResult(pagina: number = 0, qtd: number = 10) : Observable<PagedResult<Pessoa>> {
    return this.http.get<PagedResult<Pessoa>>(this.apiBaseUrl + '?pagina=' + pagina + '&qtd=' + qtd);
  }

  getById(id: number) : Observable<Pessoa> {
    return this.http.get<Pessoa>(this.apiBaseUrl + '/' + id);
  }

  addNew(pessoa : Pessoa) : Observable<void> {
    return this.http.post<void>(this.apiBaseUrl, pessoa, this.httpOptions);
  }

  deleteById(id: number) : Observable<void> {
    return this.http.delete<void>(this.apiBaseUrl + '/' + id);
  }
}
