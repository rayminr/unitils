package org.unitils.dbunit.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从EXCEL数据集文件创建Bean
 *
 * @author rayminr
 */
public class XlsDataSetBeanFactory {

    /**
     * 从DbUnit的EXCEL数据集文件创建多个bean
     * @param testClass 测试类（为获取资源文件路径）
     * @param file excel文件（相对于testClass的资源文件）
     * @param tableName excel中sheet名（一般对应数据库中表名）
     * @param clazz 需要转化的目标Bean类
     * @param dateFormat 时间格式，传null默认为yyyy-MM-dd HH:mm:ss
     * @return <T> List<T>  转化成的目标Bean列表
     * @throws Exception
     */
    public static <T> List<T> createBeans(Class testClass, String file, String tableName, Class<T> clazz, String dateFormat) throws Exception {
        BeanUtilsBean beanUtils = createBeanUtils(dateFormat);
        List<Map<String, Object>> propsList = createProps(testClass, file, tableName);
        List<T> beans = new ArrayList<T>();
        for (Map<String, Object> props : propsList) {
            T bean = clazz.newInstance();
            beanUtils.populate(bean, props);
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 从DbUnit的EXCEL数据集文件创建单个bean，如果不存在返回null
     * @param testClass 测试类（为获取资源文件路径）
     * @param file excel文件（相对于testClass的资源文件）
     * @param tableName excel中sheet名（一般对应数据库中表名）
     * @param clazz 需要转化的目标Bean类
     * @param dateFormat 时间格式，传null默认为yyyy-MM-dd HH:mm:ss
     * @return <T> List<T>  转化成的目标Bean
     * @throws Exception
     */
    public static <T> T createBean(Class testClass, String file, String tableName, Class<T> clazz, String dateFormat) throws Exception {
        List<T> beans = createBeans(testClass, file, tableName, clazz, dateFormat);
        return beans != null ? beans.get(0) : null;
    }

    /**
     * 利用DBUnit的XlsDataSet，从Excel文件解析生成List<Map>对象，并把数据库下划线字段转成驼峰式
     * @param testClass
     * @param file
     * @param tableName
     * @return
     * @throws IOException
     * @throws DataSetException
     */
    private static List<Map<String, Object>> createProps(Class testClass, String file, String tableName) throws IOException, DataSetException {
        List<Map<String, Object>> propsList = new ArrayList<Map<String, Object>>();
        IDataSet expected = new XlsDataSet(testClass.getResourceAsStream(file));
        ITable table = expected.getTable(tableName);
        Column[] columns = table.getTableMetaData().getColumns();
        for (int i = 0; i < table.getRowCount(); i++) {
            Map<String, Object> props = new HashMap<String, Object>();
            for (Column c : columns) {
                Object value = table.getValue(i, c.getColumnName());
                String propName = underlineToCamel(c.getColumnName());
                props.put(propName, value);
            }
            propsList.add(props);
        }
        return propsList;
    }

    private static String underlineToCamel(String str) {
        String[] pattern = str.split("_");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pattern.length; i++) {
            if (i == 0) {
                builder.append(pattern[i]);
            } else {
                builder.append(pattern[i].substring(0, 1).toUpperCase());
                builder.append(pattern[i].substring(1));
            }
        }
        return builder.toString();
    }

    private static BeanUtilsBean createBeanUtils(String dateFormat) {
        ConvertUtilsBean convertUtilsBean = createConverUtils(dateFormat);
        return new BeanUtilsBean(convertUtilsBean);
    }

    private static ConvertUtilsBean createConverUtils(String dateFormat) {
        DateConverter dateConverter = new DateConverter();
        if(StringUtils.isBlank(dateFormat)){
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        dateConverter.setPattern(dateFormat);
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(dateConverter, java.util.Date.class);
        convertUtilsBean.register(dateConverter, Timestamp.class);
        convertUtilsBean.register(dateConverter, java.sql.Date.class);
        return convertUtilsBean;
    }
}
