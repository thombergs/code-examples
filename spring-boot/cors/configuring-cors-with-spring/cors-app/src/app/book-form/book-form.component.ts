import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookServiceService } from '../service/book-service.service';
import { Book } from '../model/book';

@Component({
  selector: 'app-book-form',
  templateUrl: './book-form.component.html',
  styleUrls: ['./book-form.component.css']
})
export class BookFormComponent {

  book : Book;

  constructor(
      private route: ActivatedRoute,
        private router: Router,
          private bookService: BookServiceService) {
      this.book = new Book();
    }

    onSubmit(book:Book) {
      this.bookService.save(book).subscribe(result => this.gotoBookList());
    }

    gotoBookList() {
      this.router.navigate(['/books']);
    }

}
