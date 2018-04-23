package com.ep.domain.event;

import lombok.extern.slf4j.Slf4j;
import org.weixin4j.model.message.OutputMessage;
import org.weixin4j.model.message.normal.*;
import org.weixin4j.model.message.output.TextOutputMessage;
import org.weixin4j.spi.INormalMessageHandler;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 16:44 2018/4/22
 */
@Slf4j
public class AtsNormalMessageHandler implements INormalMessageHandler {

    @Override
    public OutputMessage textTypeMsg(TextInputMessage msg) {
        log.debug("文本消息：" + msg.getContent());
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("您发的消息是：" + msg.getContent());
        return out;
    }

    @Override
    public OutputMessage imageTypeMsg(ImageInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("你的消息已经收到！");
        return out;
    }

    @Override
    public OutputMessage voiceTypeMsg(VoiceInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("你的消息已经收到！");
        return out;
    }

    @Override
    public OutputMessage videoTypeMsg(VideoInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("你的消息已经收到！");
        return out;
    }

    @Override
    public OutputMessage shortvideoTypeMsg(ShortVideoInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("你的消息已经收到！");
        return out;
    }

    @Override
    public OutputMessage locationTypeMsg(LocationInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("你的消息已经收到！");
        return out;
    }

    @Override
    public OutputMessage linkTypeMsg(LinkInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("你的消息已经收到！");
        return out;
    }
}
