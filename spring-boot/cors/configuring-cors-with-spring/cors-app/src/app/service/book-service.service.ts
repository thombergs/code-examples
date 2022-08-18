import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Book } from '../model/book';
import { Observable } from 'rxjs';

@Injectable()
export class BookServiceService {

  private bookUrl: string;

  private headers : HttpHeaders;

    constructor(private http: HttpClient) {
      this.bookUrl = 'http://localhost:8092/cors-library/managed/books';
      this.headers = new HttpHeaders().set('Requestor-Type', 'ADMIN');
    }

    public findAll(): Observable<HttpResponse<Book[]>> {
        return this.http.get<Book[]>(this.bookUrl + '?type=all', { 'headers': this.headers, observe: 'response' });
    }

    public save(book: Book) {
        console.log(book);
        return this.http.post<Book>(this.bookUrl, book);
    }
}
