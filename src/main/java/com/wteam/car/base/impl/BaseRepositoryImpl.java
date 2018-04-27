package com.wteam.car.base.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements com.wteam.car.base.BaseRepository<T, ID> {
    private final EntityManager entityManager;
    private final JpaEntityInformation<T, ID> entityInformation;
    /**
     * 复用日志
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }


    /**
     * 条件查询数据是否存在
     *
     * @param arguments 先字段名称后参数值
     * @return bool存在与否
     */
    public boolean isExists(Object... arguments) {
        int length = arguments.length;
        Assert.isTrue(length % 2 == 0, "传入的参数个数必须为偶数");
        StringBuilder sql = new StringBuilder("select count(e) from " + this.entityInformation.getEntityName() + " e where e." + arguments[0] + "= :value0");

        for (int i = 1; i < length / 2; ++i) {
            sql.append(" and ").append(arguments[i]).append("=:value").append(i);
        }

        Query query = this.entityManager.createQuery(sql.toString());


        for (int i = length / 2; i < length; ++i) {
            query.setParameter("value" + (i - length / 2), arguments[i]);
        }

        return (Long) query.getSingleResult() > 0L;
    }


    /**
     * 分页查询
     *
     * @param pageSize  分页数量
     * @param currPage  当前页
     * @param isCount   是否查询数量
     * @param criterion 完整的条件语句，用空格分隔
     * @return 数量或数据
     */
    public Object getByPage(int pageSize, int currPage, boolean isCount, Object... criterion) {
        StringBuilder sql;
        if (isCount) {
            sql = new StringBuilder("select count(e) from " + this.entityInformation.getEntityName() + " e ");
        } else {
            sql = new StringBuilder("select e from " + this.entityInformation.getEntityName() + " e ");
        }


        boolean isCriterionNull = true;
        if (criterion.length > 0) {
            isCriterionNull = false;
        }
        int length = criterion.length;
        if (!isCriterionNull) {
            Assert.isTrue((length + 1) % 4 == 0, "传入条件参数格式有误：" + Arrays.toString(criterion));
            sql.append(" where e.").append(criterion[0]).append(" ").append(criterion[1]).append(" :value0 ");

            if (length > 3) {
                for (int i = 1; i <= (length - 3) / 4; ++i) {
                    sql.append(" ").append(criterion[i * 4 - 1]).append(" e.").append(criterion[i * 4])
                            .append(" ").append(criterion[i * 4 + 1]).append(" :value").append(i);
                }
            }
        }

        TypedQuery query = null;
        if (isCount) {
            query = this.entityManager.createQuery(sql.toString(), Long.class);
        } else {
            query = this.entityManager.createQuery(sql.toString(), this.entityInformation.getJavaType());
        }

        if (!isCriterionNull) {
            for (int i = 0; i < (length + 1) / 4; ++i) {
                query.setParameter("value" + i, criterion[i * 4 + 2]);
            }
        }


        if (isCount) {
            return query.getSingleResult();
        } else {
            return query.setMaxResults(pageSize).setFirstResult((currPage - 1) * pageSize).getResultList();
        }
    }

    @Transactional
    public void resetId() {
        if (entityInformation.getIdType() == Integer.class) {
            this.entityManager.createNativeQuery("alter table `" + this.camel2Underline(this.entityInformation.getEntityName()) + "` auto_increment=1").executeUpdate();
        }
    }


    @Transactional
    public void executeSql(String... sqls) {
        for (String sql : sqls) {
            if (!com.wteam.car.utils.CommUtils.isBlank(sql)) {
                entityManager.createNativeQuery(sql).executeUpdate();
            }
        }
    }


    /**
     * 驼峰命名法转下划线
     *
     * @param source 源
     * @return 返回值
     */
    private String camel2Underline(String source) {
        if (source != null && !source.trim().isEmpty()) {
            source = source.trim();
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(source.charAt(0)).toLowerCase());

            for (int i = 1; i < source.length(); ++i) {
                String s = String.valueOf(source.charAt(i));
                if (s.toUpperCase().equals(s) && !Character.isDigit(s.charAt(0))) {
                    sb.append("_");
                }

                sb.append(s.toLowerCase());
            }

            return sb.toString();
        } else {
            return source;
        }
    }
}
