package org.demo.migrissync.dao;

import org.demo.migrissync.dao.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

  List<Booking> findByIsActiveTrue();

  List<Booking> findByEmailAndIsActiveTrue(String email);

  Optional<Booking> findByIdAndIsActiveTrue(String id);

  List<Booking> findByIdInAndIsActiveTrue(Collection<String> ids);
}