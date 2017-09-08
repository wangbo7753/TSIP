/**
 * Created by YFZX-WB on 2017/4/2.
 */
require(['../config'], function (config) {

    require(['jquery', 'common/dt', 'common/nav', 'bootstrap', 'common/slider'], function ($) {

        $(window).scroll(function () {
            if ($(window).scrollTop() > 650) {
                $(".back-top").show();
            } else {
                $(".back-top").hide();
            }
        });

    });

});