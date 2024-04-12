/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : lesson

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-05-08 10:34:59
*/
drop database if exists lesson;
create database if not exists lesson;

use lesson;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`
(
    `ID`           int(20) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
    `teachclassid` int(20) NOT NULL COMMENT '所选课程，外键',
    `no`           int(20)      DEFAULT NULL COMMENT '序号，每个学生的实验报告文件名默认格式为学号项目实验X。X为序号',
    `title`        varchar(50)  DEFAULT NULL COMMENT '序号',
    `duedate`      char(10)     DEFAULT NULL COMMENT '报告提交截止日期',
    `type`         int(20)      DEFAULT NULL COMMENT '作业类型。1-实验；2-作业',
    `note`         varchar(255) DEFAULT NULL,
    `createTime`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for classfile
-- ----------------------------
DROP TABLE IF EXISTS `classfile`;
CREATE TABLE `classfile`
(
    `ID`           int(20) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
    `teachclassid` int(20) NOT NULL COMMENT '课程，外键',
    `filename`     varchar(150) DEFAULT NULL COMMENT '文件名',
    `uploadtime`   datetime     DEFAULT NULL COMMENT '文件创建时间',
    `downloadtime` int(20)      DEFAULT NULL COMMENT '文件下载次数',
    `filesize`     int(20)      DEFAULT NULL COMMENT '文件大小',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of job
-- ----------------------------
# INSERT INTO `job`
# VALUES ('4', '6', '3', '文件', '2019-2-21', '1', '', '2019-02-20 22:02:25');
# INSERT INTO `job`
# VALUES ('5', '2', '1', '登陆', '2019-2-20', '1', '11', '2019-02-21 11:25:36');
# INSERT INTO `job`
# VALUES ('6', '2', '2', '回话操作', '2019-2-12', '1', '65', '2019-02-21 11:25:56');
# INSERT INTO `job`
# VALUES ('8', '6', '2', 'JDBC编程', '2012-6-29', '1', '', '2019-02-21 15:18:19');

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`
(
    `ID`        bigint(20)  NOT NULL AUTO_INCREMENT,
    `jobID`     int(20)     NOT NULL COMMENT '外键',
    `userid` varchar(50) NOT NULL,
    `score`     int(5)       DEFAULT NULL,
    `time`      datetime     DEFAULT NULL COMMENT '可以用来区分不同类型作业信息。如实验，可以填入提交时间',
    `note`      varchar(500) DEFAULT NULL,
    PRIMARY KEY (`ID`),
    UNIQUE KEY `uniq_job` (`jobID`, `userid`),
    CONSTRAINT `fk_jobid` FOREIGN KEY (`jobID`) REFERENCES `job` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 99
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of score
-- ----------------------------
# INSERT INTO `score`
# VALUES ('7', '6', '162210702234', null, '2019-04-21 15:50:30', null);
# INSERT INTO `score`
# VALUES ('8', '6', '162210702235', null, '2019-04-21 23:30:54', null);
# INSERT INTO `score`
# VALUES ('9', '6', '162210702201', null, '2019-04-19 18:40:46', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `userid` varchar(50) NOT NULL,
    `name`      varchar(50)  DEFAULT NULL,
    `pinyin`    varchar(100) DEFAULT NULL COMMENT '姓名的拼音，防止不认识生僻字',
    `password`  varchar(100) DEFAULT NULL,
    PRIMARY KEY (`userid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
# INSERT INTO `user`
# VALUES ('1100888', '张思', 'zhang', '1100888');
# INSERT INTO `user`
# VALUES ('1441904116', '蒋x', 'jiang', '1441904116');
# INSERT INTO `user`
# VALUES ('1441904231', '谢x', 'xie4', '1441904231');
# INSERT INTO `user`
# VALUES ('152210702113', '凌xx', 'ling', '152210702113');
# INSERT INTO `user`
# VALUES ('152210702132', '徐xx', 'xu', '152210702132');
# INSERT INTO `user`
# VALUES ('162210201321', '史xx', 'shi3', '162210201321');

-- ----------------------------
-- Table structure for userclass
-- ----------------------------
DROP TABLE IF EXISTS `userclass`;
CREATE TABLE `userclass`
(
    `ID`        int(11)     NOT NULL AUTO_INCREMENT,
    `no`        int(11)     NOT NULL COMMENT '花名册上的序号，排序用',
    `classID`   int(11)     NOT NULL COMMENT '学生所在的教学班号，外键',
    `userid` varchar(20) NOT NULL COMMENT '学号',
    `note`      varchar(255) DEFAULT NULL,
    PRIMARY KEY (`ID`),
    UNIQUE KEY `uk_userclass_noid` (`userid`, `classID`),
    KEY `fkey_classid` (`classID`),
    CONSTRAINT `fkey_classid` FOREIGN KEY (`classID`) REFERENCES `teachclass` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fkey_userid` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 871
  DEFAULT CHARSET = utf8 COMMENT ='学生和教学班号的连接表\r\n';

-- ----------------------------
-- Records of userclass
-- ----------------------------
# INSERT INTO `userclass`
# VALUES ('682', '1', '2', '1441904116', '');
# INSERT INTO `userclass`
# VALUES ('683', '2', '2', '1441904231', '');
# INSERT INTO `userclass`
# VALUES ('684', '3', '2', '152210702113', '');
# INSERT INTO `userclass`
# VALUES ('685', '4', '2', '152210702132', '');
# INSERT INTO `userclass`
# VALUES ('686', '5', '2', '162210201321', '');

-- ----------------------------
-- Table structure for teachclass
-- ----------------------------
DROP TABLE IF EXISTS `teachclass`;
CREATE TABLE `teachclass`
(
    `teachclassno`   varchar(20) NOT NULL,
    `coursename`     varchar(100) DEFAULT NULL,
    `coursesemester` char(11)    NOT NULL,
    `credit`         tinyint(4)   DEFAULT NULL,
    `evalmethod`     varchar(50)      DEFAULT NULL,
    `teacherno`      varchar(20)  DEFAULT NULL,
    `ID`             int(11)     NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`ID`),
    UNIQUE KEY `uniclassno` (`teachclassno`, `coursesemester`),
    KEY `teacherid` (`teacherno`),
    CONSTRAINT `teacherid` FOREIGN KEY (`teacherno`) REFERENCES `teacher` (`teacherno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of teachclass
-- ----------------------------
# INSERT INTO `teachclass`
# VALUES ('SSE206', 'Computer Networking', '2022 Spring', '3', 'Exams', 'nanyh', '1');
# INSERT INTO `teachclass`
# VALUES ('SSE208', 'Computer Networking Labs', '2022 Spring', '1', 'Lab reports', 'nanyh', '2');
# INSERT INTO `teachclass`
# VALUES ('19020183b-1', 'Java高级编程', '2018-2019-2', '3', '考查', '1998000011', '6');
# INSERT INTO `teachclass`
# VALUES ('19W07014b-1', '移动终端程序开发', '2018-2019-2', '3', '考查', '1998000011', '7');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`
(
    `teacherno` varchar(20) NOT NULL,
    `name`      varchar(8)  DEFAULT NULL,
    `password`  varchar(20) DEFAULT NULL,
    `level`     tinyint(6)  DEFAULT NULL,
    PRIMARY KEY (`teacherno`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher`
VALUES ('nanyh', '南老师', 'sse206nan', '1');

-- ----------------------------
-- Table structure for timetable
-- ----------------------------
DROP TABLE IF EXISTS `timetable`;
CREATE TABLE `timetable`
(
    `no`           bigint(20)  NOT NULL AUTO_INCREMENT,
    `teachclassno` varchar(20) NOT NULL,
    `weeks`        varchar(100) DEFAULT NULL COMMENT '上课周次如1-9,12',
    `classroom`    varchar(50)  DEFAULT NULL,
    `day`          smallint(6)  DEFAULT NULL COMMENT '星期，取值0,1,2,3,4,5,6',
    `time`         smallint(6)  DEFAULT NULL COMMENT '上课时间1,2,3,4,5大节',
    PRIMARY KEY (`no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of timetable
-- ----------------------------
