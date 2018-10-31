package com.daitangroup.messenger.service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    public void save(T t);

    public void update(String id, T t);

    public Optional<T> find(String id);

    public List<T> findAll(Pageable pageable);

    public void delete(String id);
}
