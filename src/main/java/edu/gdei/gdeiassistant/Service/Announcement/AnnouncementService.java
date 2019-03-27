package edu.gdei.gdeiassistant.Service.Announcement;

import edu.gdei.gdeiassistant.Pojo.Entity.Announcement;
import edu.gdei.gdeiassistant.Repository.Mongodb.Announcement.AnnouncementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementDao announcementDao;

    /**
     * 保存通知公告信息
     *
     * @param announcement
     */
    public void SaveAnnouncement(Announcement announcement) {
        announcement.setPublishTime(new Date());
        announcementDao.InsertAnnouncement(announcement);
    }

    public Announcement QueryLatestAnnouncement() {
        return announcementDao.QueryLatestAnnouncement();
    }
}