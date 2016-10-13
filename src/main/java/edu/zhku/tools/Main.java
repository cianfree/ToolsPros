package edu.zhku.tools;

import edu.zhku.tools.exceptions.NoMatchServiceException;
import edu.zhku.tools.md5.MD5ServiceHandler;
import edu.zhku.tools.sql.excel2schema.Excel2SchemaServiceHandler;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 程序入口
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/28 14:46
 * @since 0.1
 */
public class Main {

    /**
     * 业务Key-Class Map
     */
    private static Map<String, Class<? extends ServiceHandler>> servicesClassMap = new HashMap<>();

    /**
     * 注册
     */
    static {
        // MD5加密服务
        servicesClassMap.put("md5", MD5ServiceHandler.class);
        servicesClassMap.put("e2s", Excel2SchemaServiceHandler.class);
    }

    /**
     * 获取业务处理对象
     *
     * @param serviceKey 服务Key
     * @return
     */
    private static ServiceHandler getServiceHandler(String serviceKey) {
        Class<? extends ServiceHandler> handlerClass = servicesClassMap.get(serviceKey);
        if (null == handlerClass) {
            return null;
        } else {
            try {
                return handlerClass.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * 程序入口， 第一个参数表示调用的服务，
     *
     * @param args
     */
    public static void main(String[] args) {
        String excel = System.getProperty("user.dir") + File.separator + "testdata" + File.separator + "测试数据字典.xlsx";
        args = new String[]{"e2s", "-e" + excel};
        try {
            CmdArgs arg = new CmdArgs(args);
            // 获取ServiceHandler
            ServiceHandler handler = getServiceHandler(arg.getServiceKey());
            if (null == handler) {
                throw new NoMatchServiceException();
            }
            // 处理
            handler.process(arg);
        } catch (NoMatchServiceException e) {
            // 没有相应的服务异常，显示
            showServiceList();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务异常： " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * 显示服务列表
     */
    private static void showServiceList() {
        System.out.println("本工具提供一下服务：");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("md5\t\t对字符串进行MD5加密");
        System.out.println("----------------------------------------------------------------------------------------------------------");
    }
}
