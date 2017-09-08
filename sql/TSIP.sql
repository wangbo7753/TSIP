prompt PL/SQL Developer import file
prompt Created on 2017年9月8日 by YFZX-WB
set feedback off
set define off
prompt Dropping KT_FUNCTION...
drop table KT_FUNCTION cascade constraints;
prompt Dropping KT_PERMISSION...
drop table KT_PERMISSION cascade constraints;
prompt Dropping KT_ROLE...
drop table KT_ROLE cascade constraints;
prompt Dropping KT_USER...
drop table KT_USER cascade constraints;
prompt Dropping KT_USER_LOG...
drop table KT_USER_LOG cascade constraints;
prompt Dropping KT_USER_ROLES...
drop table KT_USER_ROLES cascade constraints;
prompt Creating KT_FUNCTION...
create table KT_FUNCTION
(
  id         NUMBER not null,
  func_name  VARCHAR2(50),
  func_url   VARCHAR2(30),
  parent_id  NUMBER(2),
  func_level VARCHAR2(1),
  func_desc  VARCHAR2(100),
  show_order NUMBER(2) default 0,
  enable     NUMBER(1) default 0
)
;
comment on table KT_FUNCTION
  is '系统管理功能表';
comment on column KT_FUNCTION.func_name
  is '功能名称';
comment on column KT_FUNCTION.func_url
  is 'href后的链接名';
comment on column KT_FUNCTION.parent_id
  is '父节点id';
comment on column KT_FUNCTION.func_level
  is '功能级别';
comment on column KT_FUNCTION.func_desc
  is '功能描述';
comment on column KT_FUNCTION.show_order
  is '排序id';
comment on column KT_FUNCTION.enable
  is '删除标识1为删除';
alter table KT_FUNCTION
  add constraint PK_FUNC_ID primary key (ID);

prompt Creating KT_PERMISSION...
create table KT_PERMISSION
(
  func_id   NUMBER(4) not null,
  role_id   NUMBER(4) not null,
  perm_name VARCHAR2(20)
)
;
comment on table KT_PERMISSION
  is '系统权限表';
comment on column KT_PERMISSION.role_id
  is '角色ID';
comment on column KT_PERMISSION.perm_name
  is '规则';
alter table KT_PERMISSION
  add constraint PK_PER_ID primary key (FUNC_ID, ROLE_ID);

prompt Creating KT_ROLE...
create table KT_ROLE
(
  role_id   NUMBER(2) not null,
  role_name VARCHAR2(40),
  role_code VARCHAR2(40)
)
;
comment on table KT_ROLE
  is '用户角色表';
comment on column KT_ROLE.role_id
  is '角色ID';
comment on column KT_ROLE.role_name
  is '角色名';
comment on column KT_ROLE.role_code
  is '角色代码';
alter table KT_ROLE
  add constraint PK_ROLE primary key (ROLE_ID);

prompt Creating KT_USER...
create table KT_USER
(
  user_id      VARCHAR2(20) not null,
  user_pass    VARCHAR2(100) not null,
  user_role    NUMBER(2),
  user_name    VARCHAR2(20),
  team_id      NUMBER(2),
  group_id     NUMBER(2),
  reg_date     DATE,
  last_login   DATE,
  login_times  NUMBER(10),
  login_status NUMBER(1),
  login_ip     VARCHAR2(32),
  enabled      NUMBER(1) default 1,
  bureau_id    NUMBER(2) default 7,
  depot_id     NUMBER(3),
  idx_url      VARCHAR2(50) default 'devMap'
)
;
comment on table KT_USER
  is '用户信息表';
comment on column KT_USER.user_pass
  is '密码';
comment on column KT_USER.user_role
  is '角色，关联KT_ROLE表';
comment on column KT_USER.user_name
  is '真实姓名';
comment on column KT_USER.team_id
  is '车间';
comment on column KT_USER.group_id
  is '班组';
comment on column KT_USER.reg_date
  is '注册日期';
comment on column KT_USER.last_login
  is '上次登录日期';
comment on column KT_USER.login_times
  is '登录次数';
comment on column KT_USER.login_status
  is '登录状态';
comment on column KT_USER.login_ip
  is '登录IP';
comment on column KT_USER.enabled
  is '是否可用';
comment on column KT_USER.bureau_id
  is '路局';
comment on column KT_USER.depot_id
  is '车辆段';
comment on column KT_USER.idx_url
  is '主页面';
alter table KT_USER
  add constraint PK_USER primary key (USER_ID);

prompt Creating KT_USER_LOG...
create table KT_USER_LOG
(
  id         NUMBER(11) not null,
  user_id    VARCHAR2(20),
  login_ip   VARCHAR2(16),
  login_date DATE
)
;

prompt Creating KT_USER_ROLES...
create table KT_USER_ROLES
(
  user_id VARCHAR2(20) not null,
  role_id NUMBER(2)
)
;

prompt Disabling triggers for KT_FUNCTION...
alter table KT_FUNCTION disable all triggers;
prompt Disabling triggers for KT_PERMISSION...
alter table KT_PERMISSION disable all triggers;
prompt Disabling triggers for KT_ROLE...
alter table KT_ROLE disable all triggers;
prompt Disabling triggers for KT_USER...
alter table KT_USER disable all triggers;
prompt Disabling triggers for KT_USER_LOG...
alter table KT_USER_LOG disable all triggers;
prompt Disabling triggers for KT_USER_ROLES...
alter table KT_USER_ROLES disable all triggers;
prompt Loading KT_FUNCTION...
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (28, '新建时间同步', 'createTimeSync', 27, '3', '创建一个时间同步操作', 1, 1);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (29, '修改时间同步', 'modifyTimeSync', 27, '3', '修改一个时间同步操作', 0, 1);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (23, '故障大数据', 'faultBigData', 1, '2', '大数据分析下的故障预报', 2, 1);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (41, '添加角色', 'adduser', 17, '3', null, 0, 1);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (15, '典型故障库', 'exFault', 4, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (1, '车辆监控', 'vehicle', null, '1', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (2, '设备监控', 'device', null, '1', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (3, '信息查询', 'info', null, '1', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (4, '统计分析', 'stat', null, '1', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (5, '系统管理', 'system', null, '1', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (6, '过车监控', 'vehMonitor', 1, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (7, '综合预警', 'integratedWarning', 1, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (8, '车辆追踪', 'vehTrace', 1, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (9, '设备监控', 'devMonitor', 2, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (10, '设备维修', 'devRepair', 2, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (11, '过车查询', 'vehQuery', 3, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (12, '设备查询', 'devQuery', 3, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (13, '过车统计分析', 'vehStatAnalysis', 4, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (14, '设备维修统计', 'devrepairStat', 4, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (16, '用户管理', 'userManage', 5, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (17, '角色权限', 'authManage', 5, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (18, '操作说明', 'operateDesc', 5, '2', null, 0, 0);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (24, '新建故障分析', 'createFaultBigData', 23, '3', null, 0, 1);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (26, '扩展辅助', 'expandAsist', null, '1', '扩展辅助功能菜单', 0, 1);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (27, '时间同步', 'timeSync', 26, '2', '对系统时间进行同步', 0, 1);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (79, '人工智能', 'ai', null, '1', '人脸识别、语音识别', 0, 1);
insert into KT_FUNCTION (id, func_name, func_url, parent_id, func_level, func_desc, show_order, enable)
values (80, '人脸识别', 'faceRec', 79, '2', null, 0, 1);
commit;
prompt 27 records loaded
prompt Loading KT_PERMISSION...
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (7, 3, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (6, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (7, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (8, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (5, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (16, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (17, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (18, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (1, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (1, 4, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (6, 4, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (1, 2, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (6, 2, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (7, 2, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (8, 2, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (2, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (9, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (5, 2, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (18, 2, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (1, 3, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (6, 3, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (10, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (3, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (11, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (12, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (4, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (13, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (14, 1, null);
insert into KT_PERMISSION (func_id, role_id, perm_name)
values (15, 1, null);
commit;
prompt 29 records loaded
prompt Loading KT_ROLE...
insert into KT_ROLE (role_id, role_name, role_code)
values (1, '超级管理员', 'admin');
insert into KT_ROLE (role_id, role_name, role_code)
values (2, '5T专职', null);
insert into KT_ROLE (role_id, role_name, role_code)
values (3, '设备维修人员', null);
insert into KT_ROLE (role_id, role_name, role_code)
values (4, '监控值班员', null);
commit;
prompt 4 records loaded
prompt Loading KT_USER...
insert into KT_USER (user_id, user_pass, user_role, user_name, team_id, group_id, reg_date, last_login, login_times, login_status, login_ip, enabled, bureau_id, depot_id, idx_url)
values ('wb123', 'zg8_c', 0, '王勃', null, null, to_date('21-04-2017 10:42:15', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-09-2017 15:25:25', 'dd-mm-yyyy hh24:mi:ss'), null, 1, '127.0.0.1', 1, 7, null, 'vehMonitor');
insert into KT_USER (user_id, user_pass, user_role, user_name, team_id, group_id, reg_date, last_login, login_times, login_status, login_ip, enabled, bureau_id, depot_id, idx_url)
values ('10', '45', 6, '5T专职', null, null, to_date('22-11-2016 16:24:23', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2017 09:37:52', 'dd-mm-yyyy hh24:mi:ss'), null, 0, '0:0:0:0:0:0:0:1', 1, 7, 0, 'vehMonitor');
insert into KT_USER (user_id, user_pass, user_role, user_name, team_id, group_id, reg_date, last_login, login_times, login_status, login_ip, enabled, bureau_id, depot_id, idx_url)
values ('11', '46', 5, '设备维修员', null, null, to_date('22-11-2016 16:24:35', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2017 09:53:44', 'dd-mm-yyyy hh24:mi:ss'), null, 1, '0:0:0:0:0:0:0:1', 1, 7, 0, 'vehMonitor');
insert into KT_USER (user_id, user_pass, user_role, user_name, team_id, group_id, reg_date, last_login, login_times, login_status, login_ip, enabled, bureau_id, depot_id, idx_url)
values ('12', '47', 4, '监控值班员', null, null, to_date('22-11-2016 16:24:48', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2017 09:38:03', 'dd-mm-yyyy hh24:mi:ss'), null, 0, '0:0:0:0:0:0:0:1', 1, 7, 0, 'vehMonitor');
insert into KT_USER (user_id, user_pass, user_role, user_name, team_id, group_id, reg_date, last_login, login_times, login_status, login_ip, enabled, bureau_id, depot_id, idx_url)
values ('admin', 'nyoFknsDBI', 1, '超级管理员', null, null, to_date('09-09-2016 09:59:23', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-09-2017 10:43:30', 'dd-mm-yyyy hh24:mi:ss'), null, 1, '127.0.0.1', 1, 7, 0, 'vehMonitor');
commit;
prompt 5 records loaded
prompt Loading KT_USER_ROLES...
insert into KT_USER_ROLES (user_id, role_id)
values ('10', 2);
insert into KT_USER_ROLES (user_id, role_id)
values ('11', 3);
insert into KT_USER_ROLES (user_id, role_id)
values ('12', 4);
insert into KT_USER_ROLES (user_id, role_id)
values ('wb123', 2);
insert into KT_USER_ROLES (user_id, role_id)
values ('admin', 1);
commit;
prompt 5 records loaded
prompt Enabling triggers for KT_FUNCTION...
alter table KT_FUNCTION enable all triggers;
prompt Enabling triggers for KT_PERMISSION...
alter table KT_PERMISSION enable all triggers;
prompt Enabling triggers for KT_ROLE...
alter table KT_ROLE enable all triggers;
prompt Enabling triggers for KT_USER...
alter table KT_USER enable all triggers;
prompt Enabling triggers for KT_USER_LOG...
alter table KT_USER_LOG enable all triggers;
prompt Enabling triggers for KT_USER_ROLES...
alter table KT_USER_ROLES enable all triggers;
-- Create sequence 
create sequence FUNC_SEQ
minvalue 1
maxvalue 99999999
start with 99
increment by 1
cache 20;
-- Create sequence 
create sequence LOG_SEQ
minvalue 1
maxvalue 99999999999
start with 1481
increment by 1
cache 20;
-- Create sequence 
create sequence ROLE_SEQ
minvalue 1
maxvalue 99999999999
start with 20
increment by 1
cache 5;
set feedback on
set define on
prompt Done.
