package org.linx.apick.repository;

import org.linx.apick.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    Optional<Item> findByName(String name);
    Page<Item> findAll(Pageable pageable);
}
