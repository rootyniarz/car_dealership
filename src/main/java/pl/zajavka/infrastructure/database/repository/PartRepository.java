package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.zajavka.business.dao.PartDAO;
import pl.zajavka.infrastructure.configuration.HibernateUtil;
import pl.zajavka.infrastructure.database.entity.PartEntity;

import java.util.Objects;
import java.util.Optional;

public class PartRepository implements PartDAO {

    @Override
    public Optional<PartEntity> findBySerialNumber(String serialNumber) {
        try (Session session = HibernateUtil.getSession()) {
            if (Objects.isNull(session)) {
                throw new RuntimeException("Session is null");
            }
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<PartEntity> criteriaQuery = criteriaBuilder.createQuery(PartEntity.class);
            Root<PartEntity> root = criteriaQuery.from(PartEntity.class);

            ParameterExpression<String> param1 = criteriaBuilder.parameter(String.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("serialNumber"), param1));

            Query<PartEntity> query = session.createQuery(criteriaQuery);
            query.setParameter(param1, serialNumber);
            try {
                PartEntity result = query.getSingleResult();
                session.getTransaction().commit();
                return Optional.of(result);
            } catch (Throwable ex) {
                return Optional.empty();
            }
        }
    }
}
