/*
DataBase Encoding         : 65001/UTF-8
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告唯一id',
  `title` varchar(255) NOT NULL COMMENT '公告标题',
  `body` text NOT NULL COMMENT '公告内容',
  `top` int(11) NOT NULL COMMENT '是否置顶0 置顶 1未置顶',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of announcement
-- ----------------------------

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一博文id--主键',
  `title` varchar(255) NOT NULL COMMENT '博文标题',
  `body` text NOT NULL COMMENT '博文内容',
  `discussCount` int(11) NOT NULL COMMENT '博文评论数',
  `blogViews` int(11) NOT NULL COMMENT '博文浏览数',
  `time` datetime NOT NULL COMMENT '博文发布时间',
  `state` int(11) NOT NULL COMMENT '博文状态--0 删除 1正常',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------

-- ----------------------------
-- Table structure for blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag` (
  `blog_id` int(11) NOT NULL COMMENT '博文id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  UNIQUE KEY `blog_id` (`blog_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_tag
-- ----------------------------

-- ----------------------------
-- Table structure for code
-- ----------------------------
DROP TABLE IF EXISTS `code`;
CREATE TABLE `code` (
  `id` varchar(32) NOT NULL COMMENT '邀请码主键',
  `state` int(11) NOT NULL COMMENT '激活码状态0 未使用 1已使用 2 已删除',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code
-- ----------------------------

-- ----------------------------
-- Table structure for discuss
-- ----------------------------
DROP TABLE IF EXISTS `discuss`;
CREATE TABLE `discuss` (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '评论唯一id',
  `body` varchar(255) NOT NULL COMMENT '评论内容',
  `time` datetime NOT NULL COMMENT '评论时间',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `blog_id` int(11) NOT NULL COMMENT '博文id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of discuss
-- ----------------------------

-- ----------------------------
-- Table structure for login
-- ----------------------------
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id--主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `time` datetime NOT NULL COMMENT '最后登录时间',
  `ip` varchar(16) NOT NULL COMMENT '最后登录ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login
-- ----------------------------

-- ----------------------------
-- Table structure for mailkey
-- ----------------------------
DROP TABLE IF EXISTS `mailkey`;
CREATE TABLE `mailkey` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键id',
  `mail` varchar(50) NOT NULL COMMENT '发送邮箱',
  `code` varchar(7) NOT NULL COMMENT '邮箱验证码',
  `sendTime` datetime NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mailkey
-- ----------------------------

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言唯一id',
  `name` varchar(30) NOT NULL COMMENT '游客保存为ip地址，用户保存用户名',
  `body` varchar(255) NOT NULL COMMENT '留言主体',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '回复唯一id',
  `body` varchar(255) NOT NULL COMMENT '回复内容',
  `time` datetime NOT NULL COMMENT '回复时间',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `discuss_id` int(11) NOT NULL COMMENT '评论id',
  `reply_rootid` int(11) DEFAULT NULL COMMENT '父回复节点id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reply
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(30) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'USER');
INSERT INTO `role` VALUES ('2', 'ADMIN');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标签id--主键',
  `name` varchar(20) NOT NULL COMMENT '标签名',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户唯一id--主键',
  `name` varchar(30) NOT NULL COMMENT '用户名--不能重复',
  `password` varchar(255) NOT NULL COMMENT '用户密码',
  `mail` varchar(50) NOT NULL COMMENT '用户邮箱',
  `state` int(11) NOT NULL COMMENT '用户状态 0 封禁 1正常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '$2a$10$usmASSUxqidbn2RrQi4jdeVWUcFyTfmwZgTxSy8FIXQ5CVpm/0qEa', 'kyaa077@gmail.com', '1');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1', '1');
INSERT INTO `user_role` VALUES ('2', '1', '2');
