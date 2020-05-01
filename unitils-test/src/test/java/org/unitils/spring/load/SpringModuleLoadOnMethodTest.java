/*
 * Copyright (c) Smals
 */
package org.unitils.spring.load;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.core.Unitils;
import org.unitils.spring.SpringModule;
import org.unitils.spring.annotation.SpringApplicationContext;


/**
 * Test if the {@link SpringModule} loads the context correctly.
 *
 * @author Willemijn Wouters
 *
 * @since 3.4.3
 *
 */
@SpringApplicationContext("classpath:org/unitils/spring/profile/applicationContext-dao-test.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SpringModuleLoadOnMethodTest {

    @BeforeClass
    public static void setUpClass() {
        Properties config = Unitils.getInstance().getConfiguration();
        config.put("unitils.module.spring.className", "org.unitils.spring.load.SpringModuleLoad");
        config.put("unitils.modules", "spring");
        Unitils.getInstance().init(config);
    }


    @Test
    public void test1() {
        //just a test
    }

    @Test
    public void test2() throws Exception {

    }

    @Test
    public void test3() throws Exception {

    }

    @AfterClass
    public static void afterTest() {
        SpringModuleLoad module = Unitils.getInstance().getModulesRepository().getModuleOfType(SpringModuleLoad.class);
        Assert.assertEquals(3, module.getIndexInitialize());
        Assert.assertEquals(3, module.getIndexClose());
    }

}
