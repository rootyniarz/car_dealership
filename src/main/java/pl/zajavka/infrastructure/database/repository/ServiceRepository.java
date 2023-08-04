package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.zajavka.business.dao.ServiceDAO;
import pl.zajavka.infrastructure.configuration.HibernateUtil;
import pl.zajavka.infrastructure.database.entity.ServiceEntity;

import java.util.Objects;
import java.util.Optional;

public class ServiceRepository implements ServiceDAO {

    @Override
    public Optional<ServiceEntity> findByServiceCode(String serviceCode) {
        try (Session session = HibernateUtil.getSession()) {
            if (Objects.isNull(session)) {
                throw new RuntimeException("Session is null");
            }
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ServiceEntity> criteriaQuery = criteriaBuilder.createQuery(ServiceEntity.class);
            Root<ServiceEntity> root = criteriaQuery.from(ServiceEntity.class);

            ParameterExpression<String> param1 = criteriaBuilder.parameter(String.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("serviceCode"), param1));

            Query<ServiceEntity> query = session.createQuery(criteriaQuery);
            query.setParameter(param1, serviceCode);
            try {
                ServiceEntity result = query.getSingleResult();
                session.getTransaction().commit();
                return Optional.of(result);
            } catch (Throwable ex) {
                return Optional.empty();
            }
        }
    }
}
