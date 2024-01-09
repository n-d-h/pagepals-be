package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.service.WriteServiceDto;
import com.pagepal.capstone.entities.postgre.Chapter;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Service;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
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

    //Reader
    Reader reader1 = new Reader(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172"), "name1", 5, "genre1", "Vietnamese", "accent1",
            "url", "des1", "123", "123", "url", 123.2, "tag",
            new Date(), new Date(), new Date(), null, null, null, null, null, null,
            null, null, null);

    //Chapter
    Chapter chapter1 = new Chapter(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            12, 12L, Status.ACTIVE, null, null);

    //Service
    Service service1 = new Service(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            Double.valueOf("123"), new Date(),
            "description1", Double.valueOf("123"), 12, 12,
            3, Status.ACTIVE, null, reader1, chapter1);

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
        when(this.serviceRepository.findById((UUID) any())).thenThrow(new EntityNotFoundException("Service not found"));
        assertThrows(EntityNotFoundException.class, () -> this.serviceServiceImpl.serviceById(id));
        verify(this.serviceRepository).findById((UUID) any());
    }

    /**
     * Test method for {@link ServiceServiceImpl#createService(WriteServiceDto)}
     */
    @Test
    void canDeleteService() {
        when(this.serviceRepository.save((Service) any())).thenReturn(service2);
        when(this.serviceRepository.findById((UUID) any())).thenReturn(java.util.Optional.of(service2));
        this.serviceServiceImpl.deleteService(UUID.randomUUID());
        verify(this.serviceRepository).save((Service) any());
        verify(this.serviceRepository).findById((UUID) any());
    }
}
