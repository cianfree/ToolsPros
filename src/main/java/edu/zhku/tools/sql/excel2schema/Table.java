package edu.zhku.tools.sql.excel2schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 夏集球
 * 
 * @time 2015年6月13日 下午4:43:24
 * @version 0.1
 * @since 0.1
 */
public class Table implements Comparable<Table> {

    /**
     * 排序
     */
    private int order;

    /**
     * 中文表名
     */
    private String cnname;

    /**
     * 英文表名
     */
    private String enname;

    /**
     * 域模型名称
     */
    private String domainName;

    /**
     * 字段列表
     */
    private List<TableField> fields;

    public Table() {
        super();
    }

    public Table(int order, String cnname, String enname, String domainName) {
        super();
        this.order = order;
        this.cnname = cnname;
        this.enname = enname;
        this.domainName = domainName;
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public List<TableField> getFields() {
        return fields;
    }

    public void setFields(List<TableField> fields) {
        this.fields = fields;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * 是否有主键
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午5:07:02
     * @since 0.1
     * @return
     */
    public boolean hasPK() {
        if (fields != null && !fields.isEmpty()) {
            for (TableField field : fields) {
                if (field.isIspk()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @author 夏集球
     * @time 2015年6月13日 下午4:49:06
     * @since 0.1
     */
    @Override
    public int compareTo(Table o) {
        return this.order = o.order;
    }

    /**
     * 获取主键列表，如id,key
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午5:12:41
     * @since 0.1
     * @return
     */
    public String getPKS() {
        if (fields != null && !fields.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (TableField field : fields) {
                if (field.isIspk()) {
                    sb.append(field.getName()).append(",");
                }
            }
            return sb.toString().replaceAll(",$", "");
        }
        return null;
    }

    /**
     * 获取外键TableField
     * 
     * @author 夏集球
     * @time 2015年7月3日 上午9:50:45
     * @since 0.1
     * @return
     */
    public List<TableField> getForeignerFields() {
        if (this.fields != null && !fields.isEmpty()) {
            List<TableField> tableFields = new ArrayList<TableField>();
            for (TableField field : fields) {
                if (field.isForeignerField()) {
                    tableFields.add(field);
                }
            }
            return tableFields.isEmpty() ? null : tableFields;
        } else {
            return null;
        }

    }

}
