package io.reflectoring;

import org.springframework.data.repository.CrudRepository;

interface UserRepository extends CrudRepository<User, UserId> {

}
