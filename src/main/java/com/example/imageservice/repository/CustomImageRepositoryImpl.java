package com.example.imageservice.repository;

import com.example.imageservice.exception.DataProcessingException;
import com.example.imageservice.model.Image;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class CustomImageRepositoryImpl implements CustomImageRepository {
    private final EntityManagerFactory managerFactory;

    public CustomImageRepositoryImpl(EntityManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public List<Image> findAll(Map<String, List<Object>> params) {
        EntityManager manager = null;
        try {
            manager = managerFactory.createEntityManager();
            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
            CriteriaQuery<Image> query = criteriaBuilder.createQuery(Image.class);
            Root<Image> phoneRoot = query.from(Image.class);
            Predicate conjunctionPredicate = criteriaBuilder.conjunction();
            for (Map.Entry<String, List<Object>> entry : params.entrySet()) {
                CriteriaBuilder.In<Object> paramPredicate = criteriaBuilder
                        .in(phoneRoot.get(entry.getKey()));
                for (Object paramValue : entry.getValue()) {
                    paramPredicate.value(paramValue);
                }
                conjunctionPredicate = criteriaBuilder.and(conjunctionPredicate, paramPredicate);
            }
            query.where(conjunctionPredicate);
            return manager.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get data from DB.");
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }
}
