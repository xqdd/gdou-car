package com.wteam.car.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    boolean isExists(Object... var1);

    Object getByPage(int pageSize, int currPage, boolean isCount, Object... criterion);

    void resetId();

    void executeSql(String... sqls);
}
