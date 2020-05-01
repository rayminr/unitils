/*
 * Copyright (c) Smals
 */
package org.unitils.spring.load;

import org.unitils.spring.SpringModule;


/**
 * Extended SpringModule just for test purposes.
 *
 * @author Willemijn Wouters
 *
 * @since 3.4.3
 *
 */
public class SpringModuleLoad extends SpringModule{

    int indexInitialize, indexClose;






    /**
     * @see org.unitils.spring.SpringModule#closeApplicationContextIfNeeded(java.lang.Object)
     */
    @Override
    protected void closeApplicationContextIfNeeded(Object testObject) {
        indexClose++;
    }
    /**
     * @see org.unitils.spring.SpringModule#initialize(java.lang.Object)
     */
    @Override
    public void initialize(Object testObject) {
        getApplicationContext(testObject);
        indexInitialize++;
    }



    /**
     * @return the indexClose
     */
    public int getIndexClose() {
        return indexClose;
    }


    /**
     * @return the indexInitialize
     */
    public int getIndexInitialize() {
        return indexInitialize;
    }

}
