package com.wteam.car.base.impl;

import com.wteam.car.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseServiceImpl<T, ID extends Serializable> implements com.wteam.car.base.BaseService<T, ID> {

    /**
     * 复用日志
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private BaseRepository<T, ID> baseRepository;

    public BaseServiceImpl(BaseRepository<T, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public List<T> findAll() {
        return this.baseRepository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return this.baseRepository.findAll(pageable);
    }

    public List<T> saveAll(Iterable<T> entities) {
        this.baseRepository.resetId();
        return this.baseRepository.saveAll(entities);
    }

    public T save(T entity) {
        this.baseRepository.resetId();
        return this.baseRepository.save(entity);
    }

    public void deleteInBatch(Iterable<T> entities) {
        this.baseRepository.deleteInBatch(entities);
    }

    public T getOne(ID id) {
        return this.baseRepository.getOne(id);
    }

    public Optional<T> findById(ID id) {
        return this.baseRepository.findById(id);
    }

    public void delete(ID id) {
        this.baseRepository.deleteById(id);
    }

    public boolean isExists(Object... arguments) {
        return this.baseRepository.isExists(arguments);
    }

    public Object getByPage(int pageSize, int currPage, boolean isCount, Object... criterion) {
        return this.baseRepository.getByPage(pageSize, currPage, isCount, criterion);
    }

    public long count() {
        return this.baseRepository.count();
    }

    @Override
    public void executeSql(String... sqls) {
        baseRepository.executeSql(sqls);
    }

}
