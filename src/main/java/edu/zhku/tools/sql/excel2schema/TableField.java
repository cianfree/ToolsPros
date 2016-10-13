package edu.zhku.tools.sql.excel2schema;

import java.util.Arrays;
import java.util.List;

/**
 * @author 夏集球
 * 
 * @time 2015年6月13日 下午4:44:31
 * @version 0.1
 * @since 0.1
 */
public class TableField implements Comparable<TableField> {

    /**
     * 排序
     */
    private int order;

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性说明
     */
    private String desc;

    /**
     * 属性类型
     */
    private String type;

    /**
     * java类型
     */
    private String javaType;

    /**
     * 属性名
     */
    private String property;

    /**
     * 是否为空
     */
    private String isnull;

    /**
     * 默认值
     */
    private String defaultVal;

    /**
     * 是否是主键
     */
    private boolean ispk;

    /**
     * 是否自增
     */
    private boolean autoIncre;

    /**
     * 是否唯一
     */
    private boolean unique;

    /**
     * 关联表
     */
    private String refTable;

    /**
     * 关联字段
     */
    private String refField;

    private List<String> enums;

    private boolean foreignerField = false;

    public TableField() {
        super();
    }

    public TableField(int order, String name, String desc, String type, String isnull, String defaultVal, boolean ispk, boolean autoIncre, boolean unique) {
        super();
        this.order = order;
        this.name = name;
        this.desc = null == desc ? null : desc.replaceAll("\\s+", " ");
        this.type = type;
        this.isnull = isnull;
        this.defaultVal = defaultVal;
        this.ispk = ispk;
        this.autoIncre = autoIncre;
        this.unique = unique;
        initEnums();
    }

    public TableField(int order, String name, String desc, String type, String isnull, String defaultVal, boolean ispk, boolean autoIncre, boolean unique, String refTable,
            String refField, String javaType, String property) {
        this(order, name, desc, type, isnull, defaultVal, ispk, autoIncre, unique);
        this.refTable = refTable;
        this.refField = refField;
        this.javaType = javaType;
        this.property = property;
        if (this.refTable != null && !"".equals(this.refTable.trim()) && this.refField != null && !"".equals(this.refField.trim())) {
            this.foreignerField = true;
        }
        initEnums();
    }

    private void initEnums() {
        if (isEnum()) {
            String enumVlaue = this.type.replaceFirst("enum\\('", "").replace("'\\)", "");
            String[] enumValues = enumVlaue.split("' *, *'");
            this.enums = Arrays.asList(enumValues);
        }
    }

    public boolean isEnum() {
        return this.type.startsWith("enum");
    }

    public List<String> getEnums() {
        return this.enums;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsnull() {
        return isnull;
    }

    public void setIsnull(String isnull) {
        this.isnull = isnull;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public boolean isIspk() {
        return ispk;
    }

    public void setIspk(boolean ispk) {
        this.ispk = ispk;
    }

    public boolean isAutoIncre() {
        return autoIncre;
    }

    public void setAutoIncre(boolean autoIncre) {
        this.autoIncre = autoIncre;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    public int compareTo(TableField o) {
        return this.order - o.order;
    }

    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public String getRefField() {
        return refField;
    }

    public void setRefField(String refField) {
        this.refField = refField;
    }

    public boolean isForeignerField() {
        return foreignerField;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

}
