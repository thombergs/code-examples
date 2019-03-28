package io.reflectoring.pagination;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

interface MovieCharacterRepository extends CrudRepository<MovieCharacter, Long> {

	Page<MovieCharacter> findAllPage(Pageable pageable);

	Slice<MovieCharacter> findAllSlice(Pageable pageable);

	List<MovieCharacter> findAllSorted(Sort sort);

}
