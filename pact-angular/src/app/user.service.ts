import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {User} from './user';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {Page} from './page';
import {Injectable} from '@angular/core';

@Injectable()
export class UserService {

  private BASE_URL = '/user-service/users';

  constructor(private httpClient: HttpClient) {
  }

  /**
   * Calls the backend to create a new User resource.
   * @param {User} resource the resource to create.
   * @returns {Observable<number>} Observable of the ID the backend assigned to the new User resource.
   */
  create(resource: User): Observable<number> {
    return this.httpClient
    .post(this.BASE_URL, resource)
    .map(data => data['id']);
  }

  /**
   * Calls the backend to update an existing User resource.
   * @param {User} resource the resource containing the updated data.
   * @param {number} id ID of the resource to update.
   * @returns {Observable<HttpResponse<any>>} Observable of the HttpResponse.
   */
  update(resource: User, id: number): Observable<HttpResponse<any>> {
    return this.httpClient
    .put(`${this.BASE_URL}/${id}`, resource);
  }

  /**
   * Calls the backend to load a single User resource.
   * @param {number} id ID of the User resource to load.
   * @returns {Observable<User>} Observable of the User.
   */
  get(id: number): Observable<User> {
    return this.httpClient
    .get(`${this.BASE_URL}/${id}`);
  }

  /**
   * Calls the backend to load a paginated list of User resources.
   * @param {number} page number of the page to load.
   * @param {number} pageSize size of the pages.
   * @returns {Observable<Page<User>>} Observable of a Page of Persons.
   */
  list(page: number, pageSize: number = 10): Observable<Page<User>> {
    const params: HttpParams = new HttpParams();
    params.set('page', page.toString());
    params.set('pageSize', page.toString());
    const options = {
      params: params
    };
    return this.httpClient.get(this.BASE_URL, options);
  }


}
