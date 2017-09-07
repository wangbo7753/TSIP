# TSIP
前端：Bootstrap+requireJs，后端：Spring mvc+Spring shiro+myBatis 架构的脚手架项目
## 项目介绍
TSIP是一个后台信息管理的脚手架工程，通过填充自身业务逻辑可构建适用于不同场景的信息系统。工程前端使用BootStrap构建UI界面，使用requireJs
结合ES6进行前端模块化开发，后端使用Spring mvc接收restful风格请求，mybatis进行数据持久化，Spring对MVC各层的bean进行容器管理，项目中涉
及shiro权限控制，文件的上传下载，webSocket使用，canvas图片拼接及放大，dataTable.jquery.min.js插件的使用等内容，作为项目总结进行输出。

**项目目前的功能需求有：**
1. shiro权限控制
   * 登录用户才可访问系统
   * 不同角色用户登录成功后的有不同权限导航
   * 自定义权限拦截
2. 用户登陆成功首页面中webSocket消息推送
3. dataTable表格插件点击行相应，动态分页，行列合并，与查询框结合等数据表格展示
4. 多图拼接浏览，随意点击实现区域放大及放大镜效果
5. 文件的上传下载
