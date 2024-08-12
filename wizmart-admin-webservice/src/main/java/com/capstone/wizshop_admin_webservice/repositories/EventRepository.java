package com.capstone.wizshop_admin_webservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.wizshop_admin_webservice.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}