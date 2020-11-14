package io.reflectoring.specification.repository;

import io.reflectoring.specification.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
@RequiredArgsConstructor
public class CustomProductRepository {
    private static final Double PREMIUM_PRICE = 1000D;
    private final ProductRepository productRepository;

    public List<Product> getLowRangeProducts(List<Category> categories) {
        return productRepository.findAll(where(belongsToCategory(categories)).and(pricesAreBetween(0D, PREMIUM_PRICE)));
    }

    public List<Product> getPremiumProducts(List<Category> categories) {
        return productRepository.findAll(
                where(belongsToCategory(categories)).and(isPremium()));
    }

    public List<Product> getPremiumProducts(String name, List<Category> categories) {
        return productRepository.findAll(
                where(belongsToCategory(categories))
                        .and(nameLike(name))
                        .and(isPremium()));
    }


    public List<Product> getQueryResult(List<Filter> filters){
        if(filters.size()>0) {
            return productRepository.findAll(getSpecificationFromFilters(filters));
        }else {
            return productRepository.findAll();
        }
    }

    private Specification<Product> getSpecificationFromFilters(List<Filter> filter) {
        Specification<Product> specification = where(createSpecification(filter.remove(0)));
        for (Filter input : filter) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private Specification<Product> createSpecification(Filter input) {
        switch (input.getOperator()){
            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                        castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case NOT_EQ:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                        castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(input.getField()),
                        (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(input.getField()),
                        (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()), "%"+input.getValue()+"%");
            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField()))
                        .value(castToRequiredType(root.get(input.getField()).getJavaType(), input.getValues()));
            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if(fieldType.isAssignableFrom(Double.class)){
            return Double.valueOf(value);
        }else if(fieldType.isAssignableFrom(Integer.class)){
            return Integer.valueOf(value);
        }else if(Enum.class.isAssignableFrom(fieldType)){
            return Enum.valueOf(fieldType, value);
        }
        return null;
    }

    private Object castToRequiredType(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }

    private Specification<Product> nameLike(String name){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%"+name+"%");
    }


    private Specification<Product> pricesAreBetween(Double min, Double max){
        return (root, query, criteriaBuilder)-> criteriaBuilder.between(root.get(Product_.PRICE), min, max);
    }

    private Specification<Product> belongsToCategory(List<Category> categories){
        return (root, query, criteriaBuilder)-> criteriaBuilder.in(root.get(Product_.CATEGORY)).value(categories);
    }

    private Specification<Product> isPremium() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get(Product_.MANUFACTURING_PLACE).get(Address_.STATE),
                                STATE.CALIFORNIA),
                        criteriaBuilder.greaterThanOrEqualTo(root.get(Product_.PRICE), PREMIUM_PRICE));
    }
}
