package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CategoryServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CategoryServiceImplTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    /**
     * Method under test: {@link CategoryServiceImpl#getCategories()}
     */
    @Test
    void canGetCategories() {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(categoryServiceImpl.getCategories().isEmpty());
        verify(categoryRepository).findAll();
    }
}

