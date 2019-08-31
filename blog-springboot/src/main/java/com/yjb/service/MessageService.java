package com.yjb.service;

import com.yjb.mapper.MessageMapper;
import com.yjb.mapper.UserMapper;
import com.yjb.model.pojo.Message;
import com.yjb.model.pojo.User;
import com.yjb.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;


    public void saveMessage(String messageBody) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        if (username == null || username =="") {
            throw new RuntimeException("用户不存在");
        }
        User user = userMapper.findUserByName(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        Message message = new Message();
        message.setName(user.getName());
        message.setBody(messageBody);
        messageMapper.saveMessage(message);
    }

    /**
     * 获取留言数量
     *
     * @return
     */
    public Long getMessageCount() {
        return messageMapper.getMessageCount();
    }

    public List<Message> findMessage(Integer page, Integer showCount) {
        List<Message> messages = messageMapper.findMessage((page - 1) * showCount, showCount);
        return messages;
    }

    public void deleteMessageById(Long messageId) {
        messageMapper.deleteMessageById(messageId);
    }
}
