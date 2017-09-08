/**
 *  created by wb7753 on 2017/9/7
 */
define(function () {

    // 获取当前网址，如：http://localhost:8080/TSIP/portal.html
    var currentPath = window.document.location.href;
    // 获取主机地址之后的目录，如：/TSIP/portal.html
    var pathName = window.document.location.pathname;
    var pos = currentPath.indexOf(pathName);
    // 获取主机地址，如：http://localhost:8080
    var basePath = currentPath.substring(0, pos);
    if (pathName.toUpperCase().indexOf('TSIP') !== -1) {
        basePath += pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    }

    require.config({
        baseUrl: basePath + '/js/lib',
        paths: {
            'jquery': 'jquery/jquery.min',
            'bootstrap': 'bootstrap/bootstrap.min',
            'datatables.net': 'dataTables/jquery.dataTables.min',
            'datatables': 'dataTables/dataTables.bootstrap.min',
            'zTree': 'zTree/jquery.ztree.all.min',
            'common': '../common'
        },
        shim: {
            'bootstrap': ['jquery'],
            'zTree': ['jquery']
        }
    });

    return {
        basePath: basePath
    }

});