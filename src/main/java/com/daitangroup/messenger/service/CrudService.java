package com.daitangroup.messenger.service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    void save(T t);

    void update(String id, T t);

    Optional<T> find(String id);

    List<T> findAll(Pageable pageable);

    void delete(String id);
}
