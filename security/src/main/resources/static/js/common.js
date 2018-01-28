/**
 * 参数解释：
 * title   标题
 * url     请求的url
 * id      需要操作的数据id
 * w       弹出层宽度（缺省调默认值）
 * h       弹出层高度（缺省调默认值）
*/
var $;
layui.use('jquery', function () {
    $ = layui.jquery;
});
function urlMode(title, url, w, h) {
    if (!title) {
        title = false;
    }
    if (!url) {
        url = "404.html";
    }
    if (!w) {
        w = ($(window).width() * 0.9);
    }
    if (!h) {
        h = ($(window).height() - 50);
    }
    layer.open({
        type: 2,
        area: [w + 'px', h + 'px'],
        fix: false, //不固定
        maxmin: true,
        shadeClose: true,
        shade: 0.4,
        title: title,
        content: url
    })
}

/**模态窗口*/
function mode(title, select, w, h) {
    var area;
    if (!title) {
        title = false;
    }
    if (!select) {
        select = "404.html";
    }
    if (!w) {
        w = ($(window).width() * 0.9);
    }
    if (!h) {
        area = w + 'px';
    }else {
        area = [w + 'px', h + 'px']
    }
    layer.open({
        type: 1,
        area: area,
        fix: false, //不固定
        maxmin: true,
        shadeClose: true,
        shade: 0.4,
        title: title,
        content: select
    })
}

/**关闭弹出框口*/
function modeUrlClose() {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}

/**关闭弹出框口*/
function modeClose() {
    layer.close(layer.index)
}

/**ajax请求*/
function ajaxPost(url,data,success) {
    $.ajax({
        url:url,
        method:'post',
        data:data,
        dataType:'json',
        success:success,
        error:function () {
            layer.msg('操作失败！')
        }
    })
}
