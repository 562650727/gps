package com.lhn.gps.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchVO {
    private GroupInfo groupInfo;
    private List<FileInfos> fileInfos;
    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setFileInfos(List<FileInfos> fileInfos) {
        this.fileInfos = fileInfos;
    }
    public List<FileInfos> getFileInfos() {
        return fileInfos;
    }

    @Data
    @Builder
    public static class GroupInfo {
        private String intention;
        private List<String> filterCondition;
    }

    @Data
    @Builder
    public static class Files {
        private String uploadUserId;
        private String uploadUserName;
        private String uploadUserAvatorUrl;
        private String fileName;
        private String fileType;
        private String aisle;
        private Date uploadTime;
    }

    @Data
    @Builder
    public static class FileInfos {
        private String filedType;
        private List<String> showInfo;
        private Date uploadTime;
        private String fileName;
        private List<Files> files;
    }
}
