package org.chinasb.common.db.dao.impl;

import java.io.Serializable;
import java.util.Collection;

import org.chinasb.common.db.dao.CommonDao;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

/**
 * 通用DAO
 * 
 * @author zhujuan
 */
@Service
public class CommonDaoImpl implements CommonDao {

    protected HibernateTemplate hibernateTemplate;

    @Autowired
    public void setSessionFactory0(SessionFactory sessionFactory) {
        if ((hibernateTemplate == null)
                || (sessionFactory != hibernateTemplate.getSessionFactory())) {
            hibernateTemplate = new HibernateTemplate(sessionFactory);
        }
    }

    public final SessionFactory getSessionFactory() {
        return hibernateTemplate != null ? hibernateTemplate.getSessionFactory() : null;
    }

    protected final Session getSession() throws DataAccessResourceFailureException,
            IllegalStateException {
        return getSessionFactory().getCurrentSession();
    }

    public <T> Criteria createCriteria(Class<T> entityClazz) {
        return getSession().createCriteria(entityClazz);
    }

    @Override
    public <T> T get(Serializable id, Class<T> entityClazz) {
        return hibernateTemplate.get(entityClazz, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void save(T... entities) {
        for (T entity : entities) {
            hibernateTemplate.save(entity);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void update(T... entities) {
        for (T entity : entities) {
            hibernateTemplate.update(entity);
        }
    }

    @Override
    public <T> void update(Collection<T> entities) {
        for (T entity : entities) {
            hibernateTemplate.update(entity);
        }
    }

    @Override
    public <T> void delete(Serializable id, Class<T> entityClazz) {
        T entity = get(id, entityClazz);
        if (entity != null) {
            hibernateTemplate.delete(entity);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(DetachedCriteria detachedCriteria) {
        if (detachedCriteria != null) {
            Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
            return (T) criteria.list();
        }
        return null;
    }

    @Override
    public int execute(String sql) {
        if (!Strings.isNullOrEmpty(sql)) {
            SQLQuery sqlQuery = getSession().createSQLQuery(sql);
            return sqlQuery.executeUpdate();
        }
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T query(String sql) {
        if (!Strings.isNullOrEmpty(sql)) {
            SQLQuery sqlQuery = getSession().createSQLQuery(sql);
            return (T) sqlQuery.list();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T query(String sql, Class<T> entityClazz) {
        if (!Strings.isNullOrEmpty(sql)) {
            SQLQuery sqlQuery = getSession().createSQLQuery(sql);
            sqlQuery.addEntity(entityClazz);
            return (T) sqlQuery.list();
        }
        return null;
    }
}
