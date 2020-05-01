/*
 * Copyright (c) Smals
 */
package org.unitils.spring.load;
import static org.junit.Assert.assertEquals;
import static org.unitils.spring.enums.LoadTime.CLASS;

import org.junit.Before;
import org.junit.Test;
import org.unitils.spring.SpringModule;
import org.unitils.spring.annotation.LoadOn;
import org.unitils.spring.enums.LoadTime;


/**
 * test {@link SpringModule#findLoadTime(Class)}.
 *
 * @author Willemijn Wouters
 *
 * @since 3.4.3
 *
 */
public class SpringModule_FindLoadTimeTest {


    private SpringModule sut;

    @Before
    public void setUp() {
        sut = new SpringModule();
    }

    @Test
    public void testFindLoadTime() throws Exception {
        assertEquals(CLASS, sut.findLoadTime(TestClass1.class));
    }

    @LoadOn(load = CLASS)
    private class TestClass1 {
        
    }


}
