import {TestBed} from '@angular/core/testing';
import {HttpClientModule} from '@angular/common/http';
import {UserService} from './user.service';
import {User} from './user';
import * as Pact from 'pact-web';

describe('UserService', () => {

  let provider;

  beforeAll(function (done) {
    provider = Pact({
      consumer: 'ui',
      provider: 'userservice',
      port: 1234,
      host: '127.0.0.1',
    });

    // required for slower CI environments
    setTimeout(done, 2000);

    // Required if run with `singleRun: false`
    provider.removeInteractions();
  });

  afterAll(function (done) {
    provider.finalize()
    .then(function () {
      done();
    }, function (err) {
      done.fail(err);
    });
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ],
      providers: [
        UserService
      ],
    });
  });

  afterEach((done) => {
    provider.verify().then(done, e => done.fail(e));
  });

  describe('create()', () => {

    const expectedUser: User = {
      firstName: 'Arthur',
      lastName: 'Dent'
    };

    const createdUserId = 42;

    beforeAll((done) => {
      provider.addInteraction({
        state: `provider accepts a new person`,
        uponReceiving: 'a request to POST a person',
        withRequest: {
          method: 'POST',
          path: '/user-service/users',
          body: expectedUser,
          headers: {
            'Content-Type': 'application/json'
          }
        },
        willRespondWith: {
          status: 201,
          body: Pact.Matchers.somethingLike({
              id: createdUserId
          }),
          headers: {
            'Content-Type': 'application/json'
          }
        }
      }).then(done, error => done.fail(error));
    });

    it('should create a Person', (done) => {
      const userService: UserService = TestBed.get(UserService);
      userService.create(expectedUser).subscribe(response => {
        expect(response).toEqual(createdUserId);
        done();
      }, error => {
        done.fail(error);
      });
    });

  });

});
