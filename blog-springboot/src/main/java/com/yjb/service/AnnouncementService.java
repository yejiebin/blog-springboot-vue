package com.yjb.service;

import com.yjb.mapper.AnnouncementMapper;
import com.yjb.model.pojo.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    /**
     * 获取公告数量
     *
     * @return
     */
    public Long getAnnouncementCount() {
        return announcementMapper.getAnnouncementCount();
    }

    /**
     * 分页查询公告
     *
     * @param page
     * @param showCount
     * @return
     */
    public List<Announcement> findAnnouncement(Integer page, Integer showCount) {
        return announcementMapper.findAnnouncement((page-1)*showCount, showCount);
    }

    /**
     * 置顶或取消置顶公告
     *
     * @param announcementId
     * @param top
     */
    public void updateAnnouncementTop(Long announcementId, Integer top) {
        Announcement announcement = new Announcement();
        announcement.setId(announcementId);
        announcement.setTop(top);
        announcementMapper.updateAnnouncementTop(announcement);
    }

    /**
     * 发布公告
     *
     * @param title
     * @param body
     */
    public void saveAnnouncement(String title, String body) {
        body = body.replaceAll("\n", "<br />");
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setBody(body);
        announcement.setTop(1);
        announcementMapper.saveAnnouncement(announcement);
    }

    /**
     * 根据公告id删除公告
     *
     * @param announcementId
     */
    public void deleteAnnouncementById(Long announcementId) {
        announcementMapper.deleteAnnouncementById(announcementId);
    }
}
