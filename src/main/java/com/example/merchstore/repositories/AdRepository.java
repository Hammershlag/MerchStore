package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Ad;
import com.example.merchstore.components.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 26.08.2024
 */

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    //Find
    public List<Ad> findAllByItem(Item item);


}
