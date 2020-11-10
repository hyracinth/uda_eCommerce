package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems() {
        Item item = new Item();
        item.setId(1L);
        item.setName("newItem");
        item.setPrice(BigDecimal.valueOf(1000.0));
        item.setDescription("newDescription");

        List<Item> listItems = new ArrayList<>();
        listItems.add(item);

        when(itemRepository.findAll()).thenReturn(listItems);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returnedItems = response.getBody();
        Item returnedItem = returnedItems.get(0);
        assertEquals("newItem", returnedItem.getName());
        assertEquals("newDescription", returnedItem.getDescription());
    }

    @Test
    public void getItemById() {
        Long id = 1L;
        Item item = new Item();
        item.setId(id);
        item.setName("newItem");
        item.setPrice(BigDecimal.valueOf(1000.0));
        item.setDescription("newDescription");

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item returnedItem = response.getBody();
        assertEquals("newItem", returnedItem.getName());
        assertEquals("newDescription", returnedItem.getDescription());
        assertEquals(id, returnedItem.getId());
    }

    @Test
    public void getItemsByName() {
        String name = "newItem";
        Item item = new Item();
        item.setId(1L);
        item.setName(name);
        item.setPrice(BigDecimal.valueOf(1000.0));
        item.setDescription("newDescription");

        List<Item> listItems = new ArrayList<>();
        listItems.add(item);

        when(itemRepository.findByName(name)).thenReturn(listItems);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(name);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returnedItems = response.getBody();
        Item returnedItem = returnedItems.get(0);
        assertEquals("newItem", returnedItem.getName());
        assertEquals("newDescription", returnedItem.getDescription());
    }
}

