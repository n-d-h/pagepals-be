package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Service;
import com.pagepal.capstone.entities.postgre.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, UUID> {
    @Query("SELECT DISTINCT st FROM ServiceType st WHERE EXISTS (SELECT s FROM st.services s WHERE s IN ?1)")
    List<ServiceType> findAllByServices(List<Service> services);
}
