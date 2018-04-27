package com.wteam.car.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID extends Serializable> {
    List<T> findAll();

    Page<T> findAll(Pageable var1);

    List<T> saveAll(Iterable<T> var1);

    T save(T var1);

    void deleteInBatch(Iterable<T> var1);

    T getOne(ID var1);

    Optional<T> findById(ID var1);

    void delete(ID var1);

    boolean isExists(Object... var1);

    Object getByPage(int var1, int var2, boolean var3, Object... var4);

    long count();

    void executeSql(String... sqls);

}
