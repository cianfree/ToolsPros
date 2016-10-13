package edu.zhku.tools;

import edu.zhku.tools.exceptions.NoMatchServiceException;
import edu.zhku.tools.exceptions.ServiceException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * 命令行参数对象，用于解析参数对象， 命令行使用 '-' + (key) + '=' + (value)
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/1/15 16:33
 * @since 0.1
 */
public class CmdArgs {

    /**
     * 参数map，记录key-value
     */
    private Map<String, String> argsMap = new HashMap<>();

    /**
     * 服务key，使用第一个命令行参数作为服务key
     */
    private String serviceKey;

    /**
     * 默认构造函数
     *
     * @param args
     */
    public CmdArgs(String[] args) {
        if (null == args || args.length < 1) {
            throw new NoMatchServiceException("请输入服务标识！");
        }
        // 服务key
        this.serviceKey = args[0];
        // 解析其他参数, 注意，不是以 '-' 开头的将视为参数不合法
        if (args.length > 1) {
            for (int i = 1; i < args.length; ++i) {
                parseArg(args[i]);
            }
        }
    }

    /**
     * 获取ServiceKey
     *
     * @return
     */
    public String getServiceKey() {
        return serviceKey;
    }

    /**
     * 获取指定参数字符串
     *
     * @param key          参数的key
     * @param defaultValue 默认值， 如果获取的是null就返回该值
     * @return 如果不存在就返回null，否则返回对应key的值
     */
    public String getString(String key, String defaultValue) {
        if (null != key && this.argsMap.containsKey(key)) {
            String value = this.argsMap.get(key);
            if (null == value || "".equals(value.trim())) {
                return defaultValue;
            }
            return value;
        }
        return defaultValue;
    }

    /**
     * 获取整型
     *
     * @param key          参数的Key
     * @param defaultValue 默认值
     * @return
     */
    public Integer getInt(String key, Integer defaultValue) {
        String intString = getString(key, null);
        if (null == intString) {
            return defaultValue;
        }
        return Integer.parseInt(intString);
    }

    /**
     * 获取Long
     *
     * @param key          参数的Key
     * @param defaultValue 默认值
     * @return
     */
    public Long getLong(String key, Long defaultValue) {
        String longString = getString(key, null);
        if (null == longString) {
            return defaultValue;
        }
        return Long.parseLong(longString);
    }

    /**
     * 获取时间参数
     *
     * @param key          参数的Key
     * @param defaultValue 默认值
     * @return
     */
    public Date getTime(String key, Date defaultValue) {
        Date time = DateFormat.getDate(key);
        return null == time ? defaultValue : time;
    }

    /**
     * 解析参数选项， 非 '-' 开头的，直接抛出异常
     *
     * @param arg 要解析的参数
     */
    private void parseArg(String arg) {
        if (null == arg || !arg.startsWith("-") || arg.length() < 3) {
            throw new ServiceException("参数格式不正确：" + arg);
        }
        // 解析Key
        String key = String.valueOf(arg.charAt(1));
        String value = arg.substring(2);
        argsMap.put(key, value);
    }

    /**
     * 显示参数
     */
    public void showArgs() {
        System.out.println("显示参数 BEG===========================================================");
        System.out.println(">> 服务标识：" + serviceKey);
        Set<Map.Entry<String, String>> entrySet = argsMap.entrySet();
        if (!entrySet.isEmpty()) {
            System.out.println(">> 参数列表：");
            for (Map.Entry<String, String> entry : entrySet) {
                System.out.println(">> " + entry.getKey() + ":\t" + entry.getValue());
            }
        } else {
            System.out.println(">> 没有参数选项！");
        }
        System.out.println("显示参数 ENG ===========================================================");
    }
}
