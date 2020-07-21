package ru.fjfalcon.service.impl;

import ru.fjfalcon.service.CollectionServiceTest;
import ru.fjfalcon.service.CollectionServiceTestDataProvider;

public class CollectionServiceStreamTest extends CollectionServiceTest {
    @Override
    public void setUp() {
        collectionService = new CollectionServiceStreamsImpl();
        dataProvider = new CollectionServiceTestDataProvider();
    }
}
