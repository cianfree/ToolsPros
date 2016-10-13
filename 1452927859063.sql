-- 删除表：服务商用户表
DROP TABLE IF EXISTS `agent`;
-- 创建表：服务商用户表
CREATE TABLE `agent` (
   `id` char(36) NOT NULL COMMENT '唯一主键',
   `firstname` varchar(10) COMMENT '姓',
   `lastname` varchar(25) COMMENT '名',
   `account` varchar(30) UNIQUE COMMENT '用户帐号',
   `password` char(32) NOT NULL COMMENT '用户密码',
   `create_time` datetime COMMENT '创建时间',
   `last_login_time` datetime COMMENT '最后一次登录时间',
   `mobile` char(11) COMMENT '手机号码',
   `email` varchar(30) COMMENT '邮件地址',
   `idcard` char(18) COMMENT '身份证号码',
   `idcard_front_url` varchar(200) COMMENT '身份证正面图片路径',
   `idcard_back_url` varchar(200) COMMENT '身份证反面图片路径',
   `realname_status` int(1) DEFAULT 1.0 COMMENT '实名审核状态： 1-待审核 2-审核通过 3-审核不通过',
   `realname_reason` varchar(200) COMMENT '实名认证审核不通过的原因',
   `bankcard_num` varchar(50) COMMENT '银行卡号码',
   `bankcard_front_url` varchar(200) COMMENT '银行卡正面图片',
   `idarea_id` int(11) COMMENT '证件地址ID',
   `idarea_details` varchar(50) COMMENT '证件地址详情',
   `tag` bit(32) NOT NULL COMMENT '标记字段，共16位，从右往左开始算： 第一位：是否大区服务商 第二位：是否省级服务商 第三位：是否市级服务商 第四位：是否县级服务商 第五位：是否项目服务商 第六位：是否镇级服务商 第七位：是否村级服务商 第八位：是否编修服务商 第九位：是否实习编修 第十位：是否正式编修 第11位：是否高级编修 第12位：是否资深编修 第13位：是否冻结 第14位：是否逻辑删除',
   `parent_editor_id` char(36) COMMENT '上级编修的ID',
   `top_editor_id` char(36) COMMENT '实习编修对应的顶级非实习编修',
   `top_surname_id` char(36) COMMENT '编修的上级姓氏服务商ID',
   `self` int(11) COMMENT 'SDT 值之 S值',
   `direction` int(11) COMMENT 'SDT 值之 D值',
   `team` int(11) COMMENT 'SDT 值之 T值',
   `approve_status` int(1) COMMENT '创建后的审核状态： 1-待审核 2-审核通过 3-审核不通过',
   `approver_id` char(36) COMMENT '审核人ID，关联员工表',
   `approve_time` datetime COMMENT '最新审核时间',
   `approve_reason` varchar(200) COMMENT '审核不通过原因',
   PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务商用户表';

-- 删除表：地区表
DROP TABLE IF EXISTS `area`;
-- 创建表：地区表
CREATE TABLE `area` (
   `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
   `name` varchar(100) NOT NULL COMMENT '地区简称，如中国',
   `full_name` varchar(200) COMMENT '地区全程，如中国湖南省长沙市',
   `idpath` varchar(50) NOT NULL COMMENT '地区的关联地区上下级ID关系，从左到右，从高到低顺序排列，之间使用/分隔',
   `type` int(1) NOT NULL COMMENT '地区类型： 1-国家 2-省级 3-市级 4-县级 5-镇级 6-村级',
   `parent_id` int(11) COMMENT '上一级的地区ID，如果为空或小于0的表示为顶级地区',
   `status` int(1) COMMENT '审核状态 1-待审核 2-审核通过 3-审核不通过',
   `code` varchar(20) COMMENT '国家统计局城市编码',
   `classify` varchar(10) COMMENT '村级编码',
   PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地区表';

-- 删除表：地区服务表
DROP TABLE IF EXISTS `agent_area`;
-- 创建表：地区服务表
CREATE TABLE `agent_area` (
   `agent_id` char(36) NOT NULL COMMENT '服务商ID',
   `area_id` int(11) NOT NULL COMMENT '地区ID',
   `agent_type` int(1) NOT NULL COMMENT '服务商类型 1-省服务商 2-市服务商 3-县服务商 4-项目服务商 5-镇服务商 6-村服务商 7-编修服务商',
   `is_present` tinyint(1) DEFAULT 0.0 COMMENT '是否赠送的， 1-是，0-否',
   `is_virtual` tinyint(1) DEFAULT 0.0 COMMENT '是否虚拟服务商， 1-是，0-否',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `official_time` datetime COMMENT '转正时间',
   PRIMARY KEY(`agent_id`,`area_id`,`agent_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地区服务表';

-- 删除表：大区表
DROP TABLE IF EXISTS `region`;
-- 创建表：大区表
CREATE TABLE `region` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一自增主键',
   `name` varchar(30) NOT NULL COMMENT '大区名称',
   `country_id` int(11) NOT NULL COMMENT '所属国家的ID',
   `agent_id` char(36) COMMENT '服务商ID，即是谁服务该大区',
   PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='大区表';

-- 删除表：大区-包含省表
DROP TABLE IF EXISTS `region_province`;
-- 创建表：大区-包含省表
CREATE TABLE `region_province` (
   `region_id` int(11) NOT NULL COMMENT '大区ID',
   `province_id` int(11) NOT NULL COMMENT '省级地区ID',
   PRIMARY KEY(`region_id`,`province_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='大区-包含省表';

-- 删除表：服务商证书表
DROP TABLE IF EXISTS `agent_licence`;
-- 创建表：服务商证书表
CREATE TABLE `agent_licence` (
   `agent_id` char(36) NOT NULL COMMENT '服务商ID',
   `show_name` varchar(50) NOT NULL COMMENT '显示名称',
   `area_id` int(11) NOT NULL COMMENT '服务地区ID',
   `licence_number` varchar(50) NOT NULL COMMENT '证书编号',
   `deadline` datetime NOT NULL COMMENT '截止日期',
   `service_type` int(2) NOT NULL COMMENT '服务类型 1-大区服务商 2-省级服务商 3-市级服务商 4-县级服务商 5-姓氏服务商 6-镇级服务商 7-村级服务商 8-实习编修服务商 9-正式编修服务商 10-高级编修服务商 11-资深编修服务商'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务商证书表';

-- 删除表：业绩用户详细信息表
DROP TABLE IF EXISTS `ach_detail`;
-- 创建表：业绩用户详细信息表
CREATE TABLE `ach_detail` (
   `member_id` char(36) NOT NULL COMMENT '唯一主键',
   `member_account` varchar(30) NOT NULL COMMENT '家谱成员帐号',
   `member_name` varchar(30) COMMENT '家谱成员的用户名',
   `member_area_idpath` varchar(50) COMMENT '家谱成员证件地址ID路径',
   `member_area_name` varchar(50) COMMENT '家谱成员证件地址全称',
   `member_submit_time` datetime COMMENT '家谱成员提交时间',
   `agent_account` varchar(30) COMMENT '编修用户的帐号',
   `agent_name` varchar(30) COMMENT '编修用户的姓名',
   `agent_area_name` varchar(50) COMMENT '编修用户的代理地区名称',
   `agent_area_id_path` varchar(50) COMMENT '编修用户的代理地区的ID路径',
   `point_regular_name` varchar(50) COMMENT '点卡规则名称',
   `point_type` int(1) COMMENT '点卡类型，0-免费，1-收费',
   `agent_id` char(36) COMMENT '编修ID',
   `agent_editor_top_id` char(36) COMMENT '编修最上级ID',
   `agent_surname_top_id` char(36) COMMENT '最上层的姓氏ID',
   `point_price` decimal(6,3) COMMENT '点卡价格',
   `is_cal_ach` tinyint(1) COMMENT '点卡是否计算业绩',
   `sale_price` double(6,3) COMMENT '点卡实价',
   `royalty_regular` int(1) COMMENT '提成计算规则 1-16.5规则 2-24规则'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业绩用户详细信息表';

-- 删除表：业绩统计信息表
DROP TABLE IF EXISTS `ach_statistic`;
-- 创建表：业绩统计信息表
CREATE TABLE `ach_statistic` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
   `agent_id` char(36) COMMENT '服务商ID',
   `agent_name` varchar(35) COMMENT '服务商名称',
   `agent_type` int(1) NOT NULL COMMENT '服务商类型: 1-大区服务商 2-省级服务商 3-市级服务商 4-县级服务商 5-项目服务商 6-镇级服务商 7-村级服务商 8-编修服务商',
   `agent_area_name` varchar(100) COMMENT '服务区域名称',
   `agent_area_id` int(11) COMMENT '服务区域ID',
   `agent_region_name` varchar(50) COMMENT '服务大区名称',
   `agent_region_id` int(11) COMMENT '服务大区ID',
   `market_local_count` int(5) NOT NULL COMMENT '市场本区人数',
   `market_outer_count` int(5) NOT NULL COMMENT '市场外区人数',
   `astr_outer_count` int(5) NOT NULL COMMENT '行政外区',
   `market_count` int(5) NOT NULL COMMENT '市场数量（市场本区+市场外区）',
   `market_royalty_rate` decimal(6,2) COMMENT '市场提成比例',
   PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业绩统计信息表';

-- 删除表：建议投诉表
DROP TABLE IF EXISTS `opinion`;
-- 创建表：建议投诉表
CREATE TABLE `opinion` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
   `title` varchar(25) NOT NULL COMMENT '标题',
   `content` text NOT NULL COMMENT '建议的内容',
   `creator_id` char(36) NOT NULL COMMENT '创建者ID，即服务商ID',
   `create_time` datetime NOT NULL COMMENT '创建时间',
   `is_replied` tinyint(1) NOT NULL DEFAULT 0.0 COMMENT '是否已回复',
   `type` int(1) NOT NULL COMMENT '类型， 1-建议， 2-投诉',
   PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建议投诉表';

-- 删除表：建议投诉回复表
DROP TABLE IF EXISTS `opinion_reply`;
-- 创建表：建议投诉回复表
CREATE TABLE `opinion_reply` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
   `opinion_id` int(11) NOT NULL COMMENT '所属建议、投诉的ID',
   `replier_id` char(36) NOT NULL COMMENT '回复者ID',
   `content` varchar(200) NOT NULL COMMENT '回复的内容',
   `reply_time` datetime NOT NULL COMMENT '回复时间',
   `target_id` int(11) COMMENT '被回复ReplyID',
   PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建议投诉回复表';

-- 删除表：建议投诉阅读表
DROP TABLE IF EXISTS `opinion_read`;
-- 创建表：建议投诉阅读表
CREATE TABLE `opinion_read` (
   `opinion_id` int(11) NOT NULL COMMENT '投诉建议ID',
   `agent_id` char(36) NOT NULL COMMENT '服务商ID',
   PRIMARY KEY(`opinion_id`,`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建议投诉阅读表';

-- 删除表：文件表
DROP TABLE IF EXISTS `document`;
-- 创建表：文件表
CREATE TABLE `document` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
   `name` varchar(50) NOT NULL COMMENT '文件名称',
   `path` varchar(200) NOT NULL COMMENT '文件相对路径',
   `uploader_id` char(36) NOT NULL COMMENT '上传用户ID,员工ID',
   `upload_time` datetime NOT NULL COMMENT '上传时间',
   `approver_id` datetime COMMENT '审核人ID， 员工ID',
   `approve_time` datetime COMMENT '审核时间',
   `status` int(1) NOT NULL COMMENT '状态： 1-未审核 2-审核通过 3-审核不通过',
   `remark` varchar(500) COMMENT '备注信息',
   PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件表';

-- 删除表：可查看文件的用户类型表
DROP TABLE IF EXISTS `document_view_type`;
-- 创建表：可查看文件的用户类型表
CREATE TABLE `document_view_type` (
   `document_id` int(11) NOT NULL COMMENT '文件ID',
   `type` int(2) NOT NULL COMMENT '可查看的用户类型各级代理，编修 1-大区服务商 2-省级服务商 3-市级服务商 4-县级服务商 5-姓氏服务商 6-镇级服务商 7-村级服务商 8-实习编修服务商 9-正式编修服务商 10-高级编修服务商 11-资深编修服务商',
   PRIMARY KEY(`document_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='可查看文件的用户类型表';

-- 删除表：公告表
DROP TABLE IF EXISTS `notice`;
-- 创建表：公告表
CREATE TABLE `notice` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一自增主键',
   `title` varchar(25) NOT NULL COMMENT '标题',
   `content` text NOT NULL COMMENT '内容',
   `publisher_id` char(36) NOT NULL COMMENT '发布者ID， 员工的ID',
   `publish_time` datetime NOT NULL COMMENT '发布时间',
   `approver_id` char(36) COMMENT '审核人ID， 员工的ID',
   `approve_time` datetime COMMENT '审核时间',
   `status` int(1) NOT NULL COMMENT '状态： 1-未审核 2-审核通过 3-审核不通过',
   `remark` varchar(50) COMMENT '备注信息',
   PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公告表';

-- 删除表：可查看的用户类型表
DROP TABLE IF EXISTS `notice_view_type`;
-- 创建表：可查看的用户类型表
CREATE TABLE `notice_view_type` (
   `notice_id` int(11) NOT NULL COMMENT '公告ID',
   `type` int(2) NOT NULL COMMENT '可查看的用户类型各级代理，编修 1-大区服务商 2-省级服务商 3-市级服务商 4-县级服务商 5-姓氏服务商 6-镇级服务商 7-村级服务商 8-实习编修服务商 9-正式编修服务商 10-高级编修服务商 11-资深编修服务商',
   PRIMARY KEY(`notice_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='可查看的用户类型表';

-- 删除表：建议投诉阅读表
DROP TABLE IF EXISTS `notice_read`;
-- 创建表：建议投诉阅读表
CREATE TABLE `notice_read` (
   `notice_id` int(11) NOT NULL COMMENT '公告ID',
   `agent_id` char(36) NOT NULL COMMENT '服务商ID',
   PRIMARY KEY(`notice_id`,`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='建议投诉阅读表';

