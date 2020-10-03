package io.reflectoring.hibernatesearch.service;

import io.reflectoring.hibernatesearch.domain.Post;
import io.reflectoring.hibernatesearch.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final EntityManager entityManager;

    public List<Post> getBasedOnLikeCountTags(Long likeCount, String hashTags, String tag){
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Post.class)
                .get();
        Query likeCountGreater = qb.range().onField("likeCount").above(likeCount).createQuery();
        Query hashTagsQuery = qb.keyword().onField("hashTags").matching(hashTags).createQuery();
        Query tagQuery = qb.keyword().onField("tag").matching(tag).createQuery();
        Query finalQuery = qb.bool().must(likeCountGreater).should(tagQuery).should(hashTagsQuery).createQuery();

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(finalQuery, Post.class);
        fullTextQuery.setSort(qb.sort().byScore().createSort());
        return (List<Post>) fullTextQuery.getResultList();
    }

    public List<Post> getPostBasedOnWord(String word){
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Post.class)
                .get();
        Query foodQuery = qb.keyword().onFields("bodyFiltered","hashTags").matching(word).createQuery();
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(foodQuery, Post.class);
        return (List<Post>) fullTextQuery.getResultList();
    }

    public List<User> getUserByFirst(String first, int max, int page){
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(User.class)
                .get();
        Query similarToUser = qb.keyword().wildcard().onField("first")
                .matching(first).createQuery();
        Query finalQuery = qb.bool().must(similarToUser).createQuery();

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(finalQuery, User.class);
        fullTextQuery.setSort(qb.sort().byField("age").desc().andByScore().createSort());
        fullTextQuery.setMaxResults(max);
        fullTextQuery.setFirstResult(page);
        return (List<User>) fullTextQuery.getResultList();
    }

    public List<User> getUserByFirstWithProjection(String first, int max, int page){
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(User.class)
                .get();
        Query similarToUser = qb.keyword().fuzzy().withEditDistanceUpTo(2).onField("first")
                .matching(first).createQuery();
        Query finalQuery = qb.bool().must(similarToUser).createQuery();

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(finalQuery, User.class);
        fullTextQuery.setProjection(FullTextQuery.ID, "first","last","middle","age");
        fullTextQuery.setSort(qb.sort().byField("age").desc().andByScore().createSort());
        fullTextQuery.setMaxResults(max);
        fullTextQuery.setFirstResult(page);
        return getUserList(fullTextQuery.getResultList());
    }

    private List<User> getUserList(List<Object[]> resultList) {
        List<User> users = new ArrayList<>();
        for (Object[] objects : resultList) {
            User user = new User();
            user.setId((String) objects[0]);
            user.setFirst((String) objects[1]);
            user.setLast((String) objects[2]);
            user.setMiddle((String) objects[3]);
            user.setAge((Integer) objects[4]);
            users.add(user);
        }
        return users;
    }
}
