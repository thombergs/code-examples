package io.reflectoring.paging;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface MovieCharacterRepository
		extends CrudRepository<MovieCharacter, Long> {

	@Query("select c from MovieCharacter c")
	Page<MovieCharacter> findAllPage(Pageable pageable);

	@Query("select c from MovieCharacter c")
	Slice<MovieCharacter> findAllSlice(Pageable pageable);

	@Query("select c from MovieCharacter c")
	List<MovieCharacter> findAllSorted(Sort sort);

	Page<MovieCharacter> findByMovie(String movieName, Pageable pageable);

	@Query("select c from MovieCharacter c where c.movie = :movie")
	Slice<MovieCharacter> findByMovieCustom(
			@Param("movie") String movieName, Pageable pageable);

	@Query("select c from MovieCharacter c where c.movie = :movie")
	List<MovieCharacter> findByMovieSorted(
			@Param("movie") String movieName, Sort sort);
}
