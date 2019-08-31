package com.yjb.mapper;

import com.yjb.model.pojo.Announcement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnnouncementMapper {

    /**
     * 获取公告数量
     *
     * @return
     */
    Long getAnnouncementCount();

    /**
     * 分页查询公告
     *
     * @param start
     * @param showCount
     * @return
     */
    List<Announcement> findAnnouncement(@Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 更新公告置顶状态
     *
     * @param announcement
     */
    void updateAnnouncementTop(Announcement announcement);

    /**
     * 保存公告
     *
     * @param announcement
     */
    void saveAnnouncement(Announcement announcement);

    /**
     * 更新公告删除状态
     *
     * @param announcementId
     */
    void deleteAnnouncementById(Long announcementId);
}
