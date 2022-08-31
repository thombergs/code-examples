import { Component, OnInit } from '@angular/core';
import { Book } from '../model/book';
import { BookServiceService } from '../service/book-service.service';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {

  books: Book[];

  constructor(private bookService : BookServiceService) { }

  ngOnInit() {
      this.bookService.findAll().subscribe((data: any) => {
        console.log('X-Get-Header : ' + data.headers.get("X-Get-Header"));
        this.books = data.body;
      });
    }


}
