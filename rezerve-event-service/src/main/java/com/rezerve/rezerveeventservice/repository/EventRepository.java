package com.rezerve.rezerveeventservice.repository;

import com.rezerve.rezerveeventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
