package com.pqrsdf.pqrsdf.generic;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.LockModeType;

@NoRepositoryBean
public interface GenericRepository<T,ID> extends JpaRepository<T,ID>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<T> findById(ID id);
}
    