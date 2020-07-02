package com.bruce.util;

import com.bruce.constant.SsmConstant;
import com.bruce.vo.ResultVO;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 发送短信工具类
 * @project: ssm
 * @package: com.bruce.util
 * @author: Bruce
 * @data: 2020/6/2822:40
 */
@Component
public class SendSMSUtil {

    @Value("${yunpian.apikey}")
    private String apikey;

    /**
     * 发短信验证码工具类方法
     * @author Bruce
     * @date 2020/6/29 0:31
     * @param phone     手机号
     * @param session   session域对象
     * @return com.bruce.vo.ResultVO
     */
    public ResultVO sendSMS(String phone, HttpSession session) {
        //3884a1900cf792f8a4df74930260e430
        //初始化clnt,使用单例方式
        YunpianClient clnt = new YunpianClient(apikey).init();
        //发送短信API
        Map<String, String> param = clnt.newParam(2);
        param.put(YunpianClient.MOBILE,phone );
        int code = (int)((Math.random()*9+1)*100000);
        param.put(YunpianClient.TEXT, "【云片网】您的验证码是" + code+"");
        Result<SmsSingleSend> r = clnt.sms().single_send(param);
        // API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
        //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg()
        Integer code1 = r.getCode();
        session.setAttribute(SsmConstant.USER_CODE,code);
        String msg = r.getMsg();
        ResultVO vo = new ResultVO(code1,msg,null);
        //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().*
        // 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*

        //释放clnt
        clnt.close();
        return vo;
    }

}
