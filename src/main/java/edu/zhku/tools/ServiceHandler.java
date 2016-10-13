package edu.zhku.tools;

/**
 * <pre>
 * 服务处理器
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/1/15 16:17
 * @since 0.1
 */
public interface ServiceHandler {

    /**
     * 处理业务
     *
     * @param args 命令行参数
     */
    void process(CmdArgs args);
}
