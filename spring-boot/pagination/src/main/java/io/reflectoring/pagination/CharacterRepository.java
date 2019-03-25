package io.reflectoring.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CharacterRepository extends CrudRepository<Character, Long> {

  List<Character> findAll(Pageable pageable);

}
