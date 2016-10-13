package edu.zhku.tools.sql.excel2schema;

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
 * 程序入口
 *
 */
public class App {
    
    /**
     * <pre>
     * -f 文件路径 
     * -s sheet的名称
     * 
     * 示例： -f
     * </pre>
     * 
     * @author 夏集球
     * @time 2015年6月15日 上午11:44:12
     * @since 0.1
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String excel = System.getProperty("user.dir") + File.separator + "testdata" + File.separator + "测试数据字典.xlsx";
        String dest = System.getProperty("user.dir") + File.separator + "testdata";
        args = new String[] {//
                "-f" + excel,//
                "-s数据字典",//
                "-e" + dest//
        };
        App app = new App();
        app.parseArgs(args);
        // 获取表对象
        List<Table> tables = app.parseTables();
        app.writeTablesToSQL(tables);
    }

    /**
     * 写入文件
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午4:52:13
     * @since 0.1
     * @param tables
     */
    public void writeTablesToSQL(List<Table> tables) throws Exception {
        String name = "数据字典_创表语句.sql";
        String path = this.exportFolder + File.separator + name;
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (Table table : tables) {
            String tableBuildSQL = TableSqlBuilder.buildTableSQL(table);
            writer.write(tableBuildSQL);
        }
        writer.close();
    }

    /**
     * 解析数据表
     * 
     * @author 夏集球
     * @time 2015年6月15日 上午11:56:17
     * @since 0.1
     * @return
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
     * @author 夏集球
     * @time 2015年6月13日 下午4:55:34
     * @since 0.1
     * @param value
     * @return
     */
    public static final boolean getBoolean(String value) {
        return "YES".equals(value);
    }

    /**
     * 是否是空行
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午4:32:59
     * @since 0.1
     * @param row
     * @return
     */
    private boolean isBlankRow(Row row) {
        Cell cell = row.getCell(0);
        String value = cell.getStringCellValue();
        return null == value || "".equals(value.trim());
    }

    /**
     * 判断指定的row是不是表的row
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午4:27:22
     * @since 0.1
     * @param row
     * @return
     */
    private boolean isTableRow(Row row) {
        Cell cell = row.getCell(2);
        if (cell == null) return true;
        String value = cell.getStringCellValue();
        return null == value || "".equals(value.trim());
    }

    /**
     * 参数解析
     * 
     * @author 夏集球
     * @time 2015年6月15日 上午11:49:57
     * @since 0.1
     * @param args
     */
    private void parseArgs(String[] args) {
        for (String arg : args) {
            String cmd = arg.substring(0, 2);
            String val = arg.substring(2);
            switch (cmd) {
            case "-f": // Excel完整路径
            case "-F":
                if (".".equals(val)) {
                    val = System.getProperty("user.dir") + File.separator;
                } else if (val.indexOf(":") < 0) {
                    val = System.getProperty("user.dir") + File.separator + val;
                }
                this.sourceFile = val;
                this.isXssf = this.sourceFile.endsWith("xlsx") || this.sourceFile.endsWith("XLSX");
                break;
            case "-e": // SQL文件输出路径
            case "-E":
                if (".".equals(val)) {
                    val = System.getProperty("user.dir") + File.separator;
                } else if (val.indexOf(":") < 0) {
                    val = System.getProperty("user.dir") + File.separator + val;
                }
                this.exportFolder = val;
                break;
            case "-s":
            case "-S":
                this.sheetName = val;
                break;
            default:
                break;
            }
        }
        // 检查参数
        if (null == this.sourceFile) {
            System.out.println("您必须输入参数: -f(Excel完整路径)");
            System.exit(1);
        }
    }

    /**
     * 源文件路径
     */
    private String sourceFile;

    /**
     * 工作簿名称
     */
    private String sheetName = "数据字典";

    /**
     * 保存结果路径
     */
    private String exportFolder = "";

    /**
     * 新版的Excel表格
     */
    private boolean isXssf = false;
}
