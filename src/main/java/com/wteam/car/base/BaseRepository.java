package com.wteam.car.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    //条件查询是否存在
    boolean isExists(Object... var1);

    //分页条件查询
    Object getByPage(int pageSize, int currPage, boolean isCount, Object... criterion);

    //重置id大小
    void resetId();

    //执行原生sql语句
    void executeSql(String... sqls);

}
