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
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.core.Unitils;
import org.unitils.spring.SpringModule;
import org.unitils.spring.annotation.LoadOn;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.enums.LoadTime;


/**
 * Test if the {@link SpringModule} loads the context correctly.
 *
 * @author Willemijn Wouters
 *
 * @since 3.4.3
 *
 */
@LoadOn(load = LoadTime.CLASS)
@SpringApplicationContext(value = "classpath:org/unitils/spring/profile/applicationContext-dao-test.xml")
@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class SpringModuleLoadOnClassTest {

    @BeforeClass
    public static void setUpClass() {
        Properties config = Unitils.getInstance().getConfiguration();
        config.put("unitils.module.spring.className", "org.unitils.spring.load.SpringModuleLoad");
        config.put("unitils.modules", "spring");
        Unitils.getInstance().init(config);

        //SpringModuleLoad module = Unitils.getInstance().getModulesRepository().getModuleOfType(SpringModuleLoad.class);

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
        Assert.assertEquals(1, module.getIndexInitialize());
        Assert.assertEquals(0, module.getIndexClose());
    }

}
