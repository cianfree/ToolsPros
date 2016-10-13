package edu.zhku.tools.md5;

import edu.zhku.tools.CmdArgs;
import edu.zhku.tools.ServiceHandler;

/**
 * <pre>
 *  MD5加密处理类
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/1/15 16:24
 * @since 0.1
 */
public class MD5ServiceHandler implements ServiceHandler {

    @Override
    public void process(CmdArgs args) {
        // 要加密的字符串
        String source = args.getString("s", null);
        Integer encodeCount = args.getInt("c", 1);
        if (encodeCount < 1) {
            encodeCount = 1;
        }
        if (null == source) {
            System.out.println("请输入要加密的字符串！");
        } else {
            System.out.println("[" + source + "] 加密[" + encodeCount + "]次后：");
            String encodeString = source;
            for (int i = 0; i < encodeCount; ++i) {
                encodeString = MD5Utils.md5Hex(encodeString);
            }
            System.out.println(encodeString);
        }
    }
}
