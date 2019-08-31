package com.yjb.service;

import com.yjb.mapper.CodeMapper;
import com.yjb.model.pojo.Code;
import com.yjb.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeService {

    @Autowired
    private CodeMapper codeMapper;

    /**
     * 生成激活码
     *
     * @return
     */
    public Code generateCode() {
        Code code = new Code();
        code.setState(0);
        code.setId(UUIDUtil.generateUUID().toUpperCase());
        codeMapper.saveCode(code);
        return code;
    }


    /**
     * 获取激活码记录条数
     *
     * @return
     */
    public Long getCodeCount() {
        return codeMapper.getCodeCount();
    }

    public List<Code> findCode(Integer page, Integer showCount) {
        return codeMapper.findCode((page - 1) * showCount, showCount);
    }
}
