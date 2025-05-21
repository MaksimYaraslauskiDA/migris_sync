package org.demo.migrissync.dao;

import org.demo.migrissync.dao.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

  Collection<Booking> findAllByEmail(String email);
}