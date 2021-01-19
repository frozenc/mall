var API = "http://localhost:8080/";

layui.use(['jquery', 'layer'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;

    // ajax全局配置
    $.ajaxSetup({
        headers: layui.data('token'),
        dataType: 'json',
        beforeSend: function (xhr, settings) {
            settings.url = API + settings.url;
        },
        error: function (xhr, textStatus, errorThrown) {
            layer.alert('出错啦...' + xhr.status + ' ' + xhr.statusText);
        },
        complete: function (xhr, textStatus) {
            if (xhr.status === 200) {
                try {
                    var res = JSON.parse(xhr.responseText);
                    if (res.code === 200) {
                        layer.msg(res.message);
                    } else {
                        layer.msg(JSON.stringify(res));
                    }
                } catch (e) {
                    // NONE
                }
            }
        }
    });

    /**
     * ajax put 封装
     */
    $.put = function (url, data, callback, dataType) {
        if ($.isFunction(data)) {
            dataType = dataType || callback;
            callback = data;
            data = undefined;
        }

        $.ajax({
            url: url,
            data: data,
            type: 'put',
            dataType: dataType,
            success: function (res) {
                callback(res);
            }
        });
    };

    /**
     * ajax delete 封装
     */
    $.delete = function (url, data, callback, dataType) {
        if ($.isFunction(data)) {
            dataType = dataType || callback;
            callback = data;
            data = undefined;
        }

        $.ajax({
            url: url,
            data: data,
            type: 'delete',
            dataType: dataType,
            success: function (res) {
                callback(res);
            }
        });
    };

    // esc关闭所有弹窗
    $(document).keydown(function (e) {
        var key = e.which;
        if (key === 27) {
            layer.closeAll();
        }
    })
});