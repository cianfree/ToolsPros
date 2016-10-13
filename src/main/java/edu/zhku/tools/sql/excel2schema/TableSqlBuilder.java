package edu.zhku.tools.sql.excel2schema;

/**
 * Table SQL 构建
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年6月15日 上午11:58:42
 * @since 0.1
 */
public class TableSqlBuilder {

    /**
     * <pre>
     * -- 删除表：员工表
     * DROP TABLE IF EXISTS employee;
     * -- 创建表：员工表
     * CREATE TABLE employee (
     *     id char(36) NOT NULL COMMENT '唯一主键：user(id), 直接取user表的ID一致',
     *     emergency_name varchar(25) NULL COMMENT '紧急联系人姓名',
     *     emergency_phone char(11) NULL COMMENT '紧急联系人电话号码',
     *     department_id int(2) NULL COMMENT '所属部门外键ID：department(id)',
     *     position_id int(2) NULL COMMENT '所属职位外键ID：position(id)',
     *     position_category_id int(2) NULL COMMENT '职位类型外键ID：position_category(id)',
     *     creator_id char(36) NULL COMMENT '创建者ID：user(id)',
     *     idarea_id int(11) NULL COMMENT '证件地址ID：area(id)',
     *     idarea_details varchar(50) NULL COMMENT '证件地址详情',
     *     PRIMARY KEY(id)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工表';
     * </pre>
     *
     * @param table
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午5:03:36
     * @since 0.1
     */
    public static final String buildTableSQL(Table table) {
        StringBuilder sb = new StringBuilder();
        String nextLine = "\n";
        sb.append("-- 删除表：").append(table.getCnname()).append(nextLine);
        sb.append("DROP TABLE IF EXISTS `").append(table.getEnname()).append("`;").append(nextLine);
        sb.append("-- 创建表：").append(table.getCnname()).append(nextLine);
        sb.append("CREATE TABLE `").append(table.getEnname()).append("` (").append(nextLine);

        int size = table.getFields().size();
        boolean hasPk = table.hasPK();
        for (int i = 0; i < size; ++i) {
            TableField field = table.getFields().get(i);
            boolean isLasted = i + 1 == size;
            sb.append(buildTableFileSQL(field, isLasted, hasPk)).append(nextLine);
        }
        if (hasPk) {    // 有主键
            sb.append("   ").append("PRIMARY KEY(`").append(table.getPKS().replaceAll(",", "`,`")).append("`)").append(nextLine);
        }
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='").append(table.getCnname()).append("';").append(nextLine);
        sb.append("\n");
        return sb.toString();
    }

    /**
     * 创建字段的SQL， name type unique isnull autoIncre comment,
     *
     * @param field
     * @param isLasted 是否最后一个属性
     * @param hasPk 是否有主键
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午5:14:34
     * @since 0.1
     */
    private static String buildTableFileSQL(TableField field, boolean isLasted, boolean hasPk) {
        StringBuilder sb = new StringBuilder("   ");
        String blank = " ";
        String unique = "UNIQUE";
        String autoIncrement = "AUTO_INCREMENT";
        sb.append("`").append(field.getName()).append("`").append(blank);
        sb.append(field.getType()).append(blank);
        if (field.isUnique()) { // 唯一
            sb.append(unique).append(blank);
        }
        if ("NOT NULL".equalsIgnoreCase(field.getIsnull())) {
            sb.append(field.getIsnull()).append(blank);
        }
        if (field.isAutoIncre()) {  // 自增
            sb.append(autoIncrement).append(blank);
        }
        if (isNotBlank(field.getDefaultVal())) {    // 默认值
            if (isNotNumber(field.getDefaultVal())) {   // 如果不是数字
                if ("NULL".equalsIgnoreCase(field.getDefaultVal())) {
                    sb.append("DEFAULT ").append(field.getDefaultVal()).append(blank);
                } else {
                    sb.append("DEFAULT '").append(field.getDefaultVal()).append("'").append(blank);
                }
            } else {
                sb.append("DEFAULT ").append(field.getDefaultVal()).append(blank);
            }
        }
        if (isNotBlank(field.getDesc())) {
            sb.append("COMMENT '").append(getCleanComment(field.getDesc())).append("'");
        }
        // 没有主键并且不是最后一个， 有主键
        if (hasPk || (!hasPk && !isLasted)){
            sb.append(",");
        }
        return sb.toString().replaceAll(" +,", ",");
    }

    /**
     * 获取注释
     *
     * @param desc
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午5:51:25
     * @since 0.1
     */
    public static final String getCleanComment(String comment) {
        return isNotBlank(comment) ? comment.replaceAll("'", "\"") : comment;
    }

    /**
     * 判断是不是数字字符串
     *
     * @param defaultVal
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午5:26:50
     * @since 0.1
     */
    public static final boolean isNotNumber(String value) {
        return !value.matches("^[0-9]*\\.?[0-9]*$");
    }

    /**
     * 是否为空
     *
     * @param value
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午5:23:40
     * @since 0.1
     */
    public static final boolean isBlank(String value) {
        return null == value || "".equals(value);
    }

    /**
     * 非空判断
     *
     * @param value
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午5:24:07
     * @since 0.1
     */
    public static final boolean isNotBlank(String value) {
        return !isBlank(value);
    }
}
