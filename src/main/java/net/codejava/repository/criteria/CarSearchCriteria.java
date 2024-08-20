package net.codejava.repository.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.SortingDTO;
import net.codejava.domain.entity.Car;

@Repository
@RequiredArgsConstructor
public class CarSearchCriteria {
    private final EntityManager entityManager;

    public PageCriteria<List<Car>> advanceSearchCar(MetaRequestDTO requestDTO) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        if (requestDTO.search() != null) {
            for (String s : requestDTO.search()) {
                Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        List<Car> cars = getCars(
                requestDTO.currentPage(),
                requestDTO.pageSize(),
                requestDTO.sortField(),
                requestDTO.sortDir(),
                criteriaList);
        Long totalCar = getTotalCars(criteriaList);

        return PageCriteria.<List<Car>>builder()
                .totalItems(totalCar)
                .totalPages((int) Math.ceil((double) totalCar / requestDTO.pageSize()))
                .currentPage(requestDTO.currentPage())
                .pageSize(requestDTO.pageSize())
                .sorting(SortingDTO.builder()
                        .sortField(requestDTO.sortField())
                        .sortDir(requestDTO.sortDir())
                        .build())
                .item(cars)
                .build();
    }

    private Long getTotalCars(List<SearchCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Car> root = query.from(Car.class);
        query = query.select(builder.count(root));

        Predicate predicate = builder.conjunction();
        SearchCriteriaConsumer queryConsumer = new SearchCriteriaConsumer(builder, predicate, root);

        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.where(predicate);
        return entityManager.createQuery(query).getSingleResult();
    }

    private List<Car> getCars(
            Integer currentPage,
            Integer pageSize,
            String sortField,
            String sortDir,
            List<SearchCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        Root<Car> root = query.from(Car.class);
        query = query.select(root);

        Predicate predicate = builder.conjunction();
        SearchCriteriaConsumer queryConsumer = new SearchCriteriaConsumer(builder, predicate, root);

        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.where(predicate);

        if (sortDir.compareTo("asc") == 0) query.orderBy(builder.asc(root.get(sortField)));
        else query.orderBy(builder.desc(root.get(sortField)));

        return entityManager.createQuery(query).getResultList();
    }
}
