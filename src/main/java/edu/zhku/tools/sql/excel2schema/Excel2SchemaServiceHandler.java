package edu.zhku.tools.sql.excel2schema;

import edu.zhku.tools.CmdArgs;
import edu.zhku.tools.ServiceHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *
 *  Excel 转成 创表语句
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/1/15 17:59
 * @since 0.1
 */
public class Excel2SchemaServiceHandler implements ServiceHandler {

    /**
     * Excel文件路径， 必须填写
     */
    private String sourceFile;

    /**
     * 要转换哪个工作簿， 如果没有填写，默认使用第一个工作簿
     */
    private String sheetName;

    /**
     * 输出目标, 如果没有填写， 默认使用当前时间.sql
     */
    private String dest;

    /**
     * 新版的Excel表格
     */
    private boolean isXssf = false;

    @Override
    public void process(CmdArgs args) {
        try {
            this.sourceFile = args.getString("e", null);
            if (null == this.sourceFile) {
                System.out.println("请输入excel数据库字典文件！");
                return;
            }
            String suffix = getFileSuffix(this.sourceFile);
            if (!".xls".equalsIgnoreCase(suffix) && !".xlsx".equalsIgnoreCase(suffix)) {
                System.out.println("请输入xls或xlsx后缀的Excel文件");
                return;
            }
            File file = new File(this.sourceFile);
            if (!file.exists()) {
                System.out.println("指定的文件不存在");
                return;
            }
            // 如果没有盘符，那么加上当前路径
            if (!this.sourceFile.contains(":")) {
                this.sourceFile = System.getProperty("user.dir") + File.separator + this.sourceFile;
            }
            System.out.println("使用源文件：" + this.sourceFile);

            this.sheetName = args.getString("s", "数据字典");
            String defaultDest = this.sourceFile.replaceAll("(\\.*)$", ".sql"); // System.getProperty("user.dir") + File.separator + System.currentTimeMillis() + ".sql";
            System.out.println(defaultDest);
            this.dest = args.getString("d", defaultDest);
            if (!this.dest.endsWith("sql")) {
                this.dest = defaultDest;
            }
            if (this.dest.endsWith(defaultDest)) {
                System.out.println("使用默认的sql文件输出路径：");
                System.out.println(defaultDest);
            }
            this.isXssf = this.sourceFile.endsWith("xlsx") || this.sourceFile.endsWith("XLSX");
            // 获取表对象
            writeTablesToSQL(parseTables());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("转换异常: " + e.getMessage());
        }
    }

    /**
     * 获取文件的后缀
     *
     * @param filepath
     * @return
     */
    private String getFileSuffix(String filepath) {
        if (null == filepath || "".equals(filepath.trim())) {
            return "";
        }
        int index = filepath.lastIndexOf(".");
        if (index > -1) {
            return filepath.substring(index);
        }
        return "";
    }

    /**
     * 写入文件
     *
     * @param tables
     * @author 夏集球
     * @time 2015年6月13日 下午4:52:13
     * @since 0.1
     */
    public void writeTablesToSQL(List<Table> tables) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.dest));
        for (Table table : tables) {
            String tableBuildSQL = TableSqlBuilder.buildTableSQL(table);
            writer.write(tableBuildSQL);
        }
        writer.close();
    }

    /**
     * 解析数据表
     *
     * @return
     * @author 夏集球
     * @time 2015年6月15日 上午11:56:17
     * @since 0.1
     */
    private List<Table> parseTables() throws Exception {
        Workbook wb;
        if (this.isXssf) {
            wb = new XSSFWorkbook(new FileInputStream(this.sourceFile));
        } else {
            wb = new HSSFWorkbook(new FileInputStream(this.sourceFile));
        }
        Sheet sheet = wb.getSheet(this.sheetName);
        if (null == sheet) {
            sheet = wb.getSheetAt(0);
        }
        int begRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();
        int tableOrder = 1;

        List<Table> tables = new ArrayList<>();

        for (int i = begRowIndex; i < lastRowIndex; ++i) {
            if (i == 70) {
                System.out.println("asd");
            }
            Row row = sheet.getRow(i);
            if (isBlankRow(row)) {  // 空行略过
                continue;
            }
            if (isTableRow(row)) {  // 如果是表行
                Cell tableNameCell = row.getCell(0);
                String tableName = tableNameCell.getStringCellValue();
                String tableCNName = tableName.replaceFirst("^[a-z_]+\\(", "").replaceAll("\\)", "").replaceFirst("-[a-zA-Z_]+$", "");
                String tableENName = tableName.replace("(" + tableCNName + ")", "").replaceFirst("-[a-zA-Z_]+$", "");
                String domainName = tableName.replaceFirst("^.*-", "");

                Table table = new Table(tableOrder++, tableCNName, tableENName, domainName);
                tables.add(table);

                // 解析字段
                int j;  // 计数器
                List<TableField> tableFields = new ArrayList<TableField>();
                table.setFields(tableFields);

                int tableFieldOrder = 1;
                for (j = i + 1; j < lastRowIndex; ++j) {
                    Row fieldRow = sheet.getRow(j);
                    if (isBlankRow(fieldRow) || isTableRow(fieldRow)) {  // 空行略过
                        i = j - 1;
                        break;
                    }
                    Cell fieldNameCell = fieldRow.getCell(0);
                    Cell fieldDescCell = fieldRow.getCell(1);
                    Cell fieldTypeCell = fieldRow.getCell(2);
                    Cell fieldNullCell = fieldRow.getCell(3);
                    Cell fieldPKCell = fieldRow.getCell(4);
                    Cell fieldDefaultCell = fieldRow.getCell(5);
                    Cell fieldAutoIncreCell = fieldRow.getCell(6);
                    Cell fieldUniqueCell = fieldRow.getCell(7);

                    tableFields.add(new TableField(//
                            tableFieldOrder++, //
                            fieldNameCell.toString(), //
                            fieldDescCell.toString(), //
                            fieldTypeCell.toString(), //
                            fieldNullCell.toString(), //
                            fieldDefaultCell.toString(), //
                            getBoolean(fieldPKCell.toString()), //
                            getBoolean(fieldAutoIncreCell.toString()),//
                            getBoolean(fieldUniqueCell.toString())));
                }
                i = j;
            }
        }
        return tables;
    }

    /**
     * 获取boolean值
     *
     * @param value
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午4:55:34
     * @since 0.1
     */
    public static final boolean getBoolean(String value) {
        return "YES".equals(value);
    }

    /**
     * 是否是空行
     *
     * @param row
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午4:32:59
     * @since 0.1
     */
    private boolean isBlankRow(Row row) {
        Cell cell = row.getCell(0);
        String value = cell.getStringCellValue();
        return null == value || "".equals(value.trim());
    }

    /**
     * 判断指定的row是不是表的row, 除了第一个单元格，其余的单元格都是空
     *
     * @param row
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午4:27:22
     * @since 0.1
     */
    private boolean isTableRow(Row row) {
        int cellCount = row.getLastCellNum() - row.getFirstCellNum() + 1;
        if (cellCount == 1) return true;
        // 获取第一个单元格
        Cell cell = row.getCell(row.getFirstCellNum());
        if (null == cell) {
            return false;
        }
        cell = row.getCell(row.getFirstCellNum() + 1);
        if (cell == null) {
            return true;
        }
        String value = cell.getStringCellValue();
        return null == value || "".equals(value.trim());
    }
}
