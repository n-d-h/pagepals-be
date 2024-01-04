package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.entities.postgre.Service;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.UUID;

@ContextConfiguration(classes = {ServiceServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ServiceServiceImplTest {
    @MockBean
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceServiceImpl serviceServiceImpl;

    //Service
    Service service1 = new Service(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), Double.valueOf("123"), new Date(),
            "description1", Double.valueOf("123"), 12, 12,
            3, Status.ACTIVE, null, null, null);

    Service service2 = new Service(UUID.randomUUID(), Double.valueOf("123"), new Date(),
            "description2", Double.valueOf("123"), 12, 12,
            3, Status.ACTIVE, null, null, null);

    /**
     * Test method for {@link ServiceServiceImpl#serviceById(UUID)}
     */

    @Test
    void canGetServiceById() {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(this.serviceRepository.findById((UUID) any())).thenReturn(java.util.Optional.of(service1));
        this.serviceServiceImpl.serviceById(id);
        verify(this.serviceRepository).findById((UUID) any());
    }

    @Test
    void shouldThrowExceptionWhenGetServiceById() {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(this.serviceRepository.findById((UUID) any())).thenThrow(new IllegalArgumentException("Service not found"));
        assertThrows(IllegalArgumentException.class, () -> this.serviceServiceImpl.serviceById(id));
        verify(this.serviceRepository).findById((UUID) any());
    }
}
