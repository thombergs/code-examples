export abstract class Page<T> {
  number: number;
  totalPages: number;
  size: number;
  totalElements: number;
  numberOfElements: number;
  last: boolean;
  first: boolean;
  content: T[];
}
