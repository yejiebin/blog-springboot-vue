package com.yjb.mapper;

import com.yjb.model.pojo.Mailkey;
import org.apache.ibatis.annotations.Param;

public interface MailkeyMapper {

    /**
     * 根据mailCode与mail查询验证码
     * @param mailCode
     * @param mail
     * @return
     */
    Mailkey findMailkeyByCodeAndMail(@Param("mailCode") String mailCode, @Param("mail") String mail);

    /**
     * 删除验证码
     * @param mail
     */
    void deleteMailkeyByMail(String mail);

    /**
     * 根据邮箱查询验证码
     * @param mail
     * @return
     */
    Mailkey findMailkeyByMail(String mail);

    /**
     * 保存验证码
     * @param mailkey
     */
    void saveMailkey(Mailkey mailkey);
}
