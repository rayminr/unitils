package org.unitils.dbunit.datasetfactory.impl;

import org.apache.commons.lang.StringUtils;
import org.unitils.core.UnitilsException;
import org.unitils.dbunit.datasetfactory.DataSetFactory;
import org.unitils.dbunit.util.MultiSchemaDataSet;
import org.unitils.dbunit.util.MultiSchemaXlsDataSetReader;

import java.io.File;
import java.util.*;

import static org.unitils.util.PropertyUtils.getString;

/**
 * A data set factory that can handle data set definitions for multiple database schemas.
 *
 * @author rayminr
 */
public class MultiSchemaXlsDataSetFactory implements DataSetFactory {

    /**
     * Property key for the dataset file extension type
     */
    public static final String PROPKEY_DATASET_FILE_EXTENSION = "DbUnitModule.DataSet.file.extension";

    /**
     * The schema name to use when no name was explicitly specified.
     */
    protected String defaultSchemaName;

    /**
     * The unitils configuration
     */
    protected Properties configuration;

    /**
     * Initializes this DataSetFactory
     *
     * @param configuration     The configuration, not null
     * @param defaultSchemaName The name of the default schema of the test database, not null
     */
    public void init(Properties configuration, String defaultSchemaName) {
        this.configuration = configuration;
        this.defaultSchemaName = defaultSchemaName;
    }

    /**
     * Creates a {@link MultiSchemaDataSet} using the given file.
     *
     * @param dataSetFiles The dataset files, not null
     * @return A {@link MultiSchemaDataSet} containing the datasets per schema, not null
     */
    public MultiSchemaDataSet createDataSet(File... dataSetFiles) {
        try {
            MultiSchemaXlsDataSetReader xlsDataSetReader = new MultiSchemaXlsDataSetReader(defaultSchemaName);
            return xlsDataSetReader.readDataSetXls(dataSetFiles);
        } catch (Exception e) {
            throw new UnitilsException("创建数据集失败：" + Arrays.toString(dataSetFiles), e);
        }
    }

    /**
     * @return The extension that files which can be interpreted by this factory must have
     */
    public String getDataSetFileExtension() {
        String ext = getString(PROPKEY_DATASET_FILE_EXTENSION, configuration);
        if(ext == null || StringUtils.isBlank(ext)){
            return "xls";
        }
        return ext;
    }
}