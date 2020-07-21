package ru.fjfalcon.service.impl;

import ru.fjfalcon.service.CollectionServiceTest;
import ru.fjfalcon.service.CollectionServiceTestDataProvider;
import ru.fjfalcon.service.impl.CollectionServiceImpl;

public class CollectionServiceArraysTest extends CollectionServiceTest {

    @Override
    public void setUp() {
        collectionService = new CollectionServiceImpl();
        dataProvider = new CollectionServiceTestDataProvider();
    }
}
