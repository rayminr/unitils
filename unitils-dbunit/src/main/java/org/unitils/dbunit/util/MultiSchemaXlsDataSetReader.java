package org.unitils.dbunit.util;

import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.unitils.core.UnitilsException;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * A reader for DbUnit excel datasets that creates a new ITable instance for each element (row).
 * <p/>
 * Excel中每个sheet对应数据库中一个表，支持多schema，sheet名为schemaName.tableName
 * Sheet中首行每列对应数据库表中对应字段，请保持一致
 * 第二行之后为对应的测试数据，注意数据格式
 * <p/>
 *
 * @author rayminr
 */
public class MultiSchemaXlsDataSetReader {

    private String pattern = ".";

    /* The schema name to use when none is specified */
    private String defaultSchemaName;

    /**
     * Creates a data set reader.
     *
     * @param defaultSchemaName The schema name to use when none is specified, not null
     */
    public MultiSchemaXlsDataSetReader(String defaultSchemaName) {
        this.defaultSchemaName = defaultSchemaName;
    }

    /**
     * Parses the datasets from the given files.
     * Each schema is given its own dataset and each row is given its own table.
     *
     * @param dataSetFiles The dataset files, not null
     * @return The read data set, not null
     */
    public MultiSchemaDataSet readDataSetXls(File... dataSetFiles) {
        try {
            Map<String, List<ITable>> tbMap = getTables(dataSetFiles);
            MultiSchemaDataSet dataSets = new MultiSchemaDataSet();

            for (Map.Entry<String, List<ITable>> entry : tbMap.entrySet()) {
                List<ITable> tables = entry.getValue();
                try {
                    DefaultDataSet ds = new DefaultDataSet(tables.toArray(new ITable[]{}));
                    dataSets.setDataSetForSchema(entry.getKey(), ds);
                } catch (AmbiguousTableNameException e) {
                    throw new UnitilsException("构造DataSet失败！", e);
                }
            }
            return dataSets;
        } catch (Exception e) {
            throw new UnitilsException("解析Excel文件出错：", e);
        }
    }

    // 需要根据schema把Table重新组合一下
    private Map<String, List<ITable>> getTables(File... dataSetFiles) {
        Map<String, List<ITable>> tableMap = new HashMap<>();

        try {
            String schema = null;
            String tableName = null;
            for (File file : dataSetFiles) {
                IDataSet dataSet = new XlsDataSet(new FileInputStream(file));
                String[] tableNames = dataSet.getTableNames();
                for (String tn : tableNames) {
                    String[] temp = tn.split(pattern);
                    if (temp.length == 2) {
                        schema = temp[0];
                        tableName = temp[1];
                    } else {
                        schema = this.defaultSchemaName;
                        tableName = tn;
                    }

                    ITable table = dataSet.getTable(tn);
                    if (!tableMap.containsKey(schema)) {
                        tableMap.put(schema, new ArrayList<ITable>());
                    }
                    tableMap.get(schema).add(new XslTableWrapper(tableName, table));
                }
            }
        } catch (Exception e) {
            throw new UnitilsException("Unable to create DbUnit dataset for data set files: " + Arrays.toString(dataSetFiles), e);
        }
        return tableMap;
    }
}
