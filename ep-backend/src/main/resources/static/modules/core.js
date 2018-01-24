// (function($) {
//     $.extend(true, $.fn.dataTable.defaults, {
//         "dom": 'T<"clear">lfrtip',
//         // "oTableTools": {//excel导出
//         //     "aButtons": []
//         // },
//         "aLengthMenu":[10,25,50,100,1000],
//         "sPaginationType": "full_numbers",
//         "bPaginate": true,
//         "oLanguage": {
//             "sProcessing": "处理中...",
//             "sLengthMenu": "显示 _MENU_ 项结果",
//             "sZeroRecords": "没有匹配结果",
//             "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
//             "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
//             "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
//             "sInfoPostFix": "",
//             "sSearch": "搜索:",
//             "sUrl": "",
//             "sEmptyTable": "表中数据为空",
//             "sLoadingRecords": "载入中...",
//             "sInfoThousands": ",",
//             "oPaginate": {
//                 "sFirst": "首页",
//                 "sPrevious": "上页",
//                 "sNext": "下页",
//                 "sLast": "末页"
//             },
//             "oAria": {
//                 "sSortAscending": ": 以升序排列此列",
//                 "sSortDescending": ": 以降序排列此列"
//             }
//         }
//     });
//
// })

// var dates = $.fn.datepicker.dates = {
//     en: {
//         days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
//         daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
//         daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"],
//         months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
//         monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
//         today: "Today",
//         clear: "Clear"
//     },
//     cn: {
//         days: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
//         daysShort: ["日", "一", "二", "三", "四", "五", "六", "七"],
//         daysMin: ["日", "一", "二", "三", "四", "五", "六", "七"],
//         months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
//         monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
//         today: "今天",
//         clear: "清除"
//     }
// };
//
// var defaults = $.fn.datepicker.defaults = {
//     autoclose: false,
//     beforeShowDay: $.noop,
//     calendarWeeks: false,
//     clearBtn: false,
//     daysOfWeekDisabled: [],
//     endDate: Infinity,
//     forceParse: true,
//     format: 'mm/dd/yyyy',
//     keyboardNavigation: true,
//     language: 'cn',
//     minViewMode: 0,
//     orientation: "auto",
//     rtl: false,
//     startDate: -Infinity,
//     startView: 0,
//     todayBtn: false,
//     todayHighlight: false,
//     weekStart: 0
// };


// var url = window.location.href;

// function getAllSearchValue(aoData) {
//     $("thead input,thead select").each(function (i) {
//         if (this.value != null && this.value != "") {
//             aoData.push({
//                 "name": this.name,
//                 "value": trimStr(this.value)
//             });
//         }
//     });
// }
// function trimStr(str) {
//     return str.replace(/(^\s*)|(\s*$)/g, "");
// }


$(function () {


    // $("#query-button").click(function () {
    //     // 判断url是否带?
    //     if(url.indexOf("?")>-1){
    //         var params = "&";
    //     }else{
    //         var params = "?";
    //     }
    //     $("#query-form input,select").each(function (i) {
    //             if (this.value != null && this.value != "") {
    //                 params = params + this.name + "=" + encodeURI(trimStr(this.value)) + "&";
    //             }
    //         }
    //     );
    //     var href = url + params + 'page=0';
    //     window.location = href;

    // });
    // $("#rest-button").click(function () {
    //     $(".query-form input,select").each(function (i) {
    //         if (this.value != null && this.value != "") {
    //            // this.removeAttr("value");
    //             this.value ="";
    //         }
    //     });
    //     // var form = $(".query-form")[0].reset();
    //     // form.reset();
    //     // $(".btnReset", form).click();
    //     // $("input[type=hidden]").removeAttr("value");
    //     // $("input[type=hidden]").removeAttr("value");//修复hidden不能清除的问题
    //     // $("input[type='password']").parents(".form-group").show();
    //     // $(".ace-switch").removeAttr("checked");//清除switch选中
    //     // var labelBox = form.find(".labelBox");
    //     // if (labelBox.length > 0) {//保持初始选中的checkbox
    //     //     labelBox.children("input[type='checkbox']").each(function () {
    //     //         var isChecked = $(this).parent().attr("checked");
    //     //         if (typeof isChecked !== "undefined" || isChecked !== "false") {
    //     //             $(this).val($(this).parent().attr("value"))
    //     //                 .attr("checked", isChecked);
    //     //         }
    //     //         ;
    //     //     });
    //     // }
    // });

});

/**
 * 根据上一页面的url,返回上一页面
 */
function getHistoryBack() {
    location.href = document.referrer;
}


var _url = window.location.href,
    url = _url.split('?')[0];
var jsonObj = parseJSON(_url);
var _obj = parseUrl(jsonObj);

function getQueryField($frm) {
    for (var item in jsonObj) {
        $frm.find("[name='" + item + "']").eq(0).val(decodeURI(jsonObj[item]));
    }
}

function parseUrl(jsonObj) {
    if (jsonObj) {
        var temp = '';
        for (var _item in jsonObj) {
            temp += _item + '=' + jsonObj[_item] + '&';
        }
        temp = temp.substring(0, temp.length - 1);
        return temp;
    } else {
        return "";
    }
}

function parseJSON(url) {
    _url = url.split('?');
    if (_url.length > 1) {
        var jsonObj = {},
            result = _url[1].split('&'),
            len = result.length;
        for (var i = 0; i < len; i++) {
            var _item = result[i],
                temp = _item.split('=');
            jsonObj[temp[0]] = temp[1];
        }
        return jsonObj;
    } else {
        return {};
    }
}

function delJSON(jsonObj, key) {
    if (jsonObj) {
        var temp = {};
        for (var _item in jsonObj) {
            if (_item == key) {
                continue;
            }
            temp[_item] = jsonObj[_item];
        }
        return temp;
    } else {
        return {};
    }
}

function getAllSearchValue(aoData) {
    $("thead input,thead select").each(function (i) {
        if (this.value != null && this.value != "") {
            aoData.push({
                "name": this.name,
                "value": trimStr(this.value)
            });
        }
    });
}

function trimStr(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

$(function () {

    // 日历组件,最小单位为秒
    $('.datetimepicker').datetimepicker({
        locale: 'zh-CN',
        format: 'YYYY-MM-DD HH:mm:ss'
    });

    // 日历组件,最小单位为分
    $('.datetimepicker_min').datetimepicker({
        locale: 'zh-CN',
        format: 'YYYY-MM-DD HH:mm:00'
    });

    //日历组件,最小单位为日
    $(".datetimepicker_date").datetimepicker({
        viewMode: 'years',
        locale: 'zh-CN',
        format: 'YYYY-MM-DD'
    });

    // 查询相关
    $("#query-button").click(function () {
        $("#query-form input,#query-form select").each(function (i) {
            if (this.value != null && this.value != "") {
                jsonObj[this.name] = this.value;
            } else {
                jsonObj = delJSON(jsonObj, this.name);
            }
        });
        jsonObj['page'] = 0;
        var href = parseUrl(jsonObj);
        if (href) {
            url += '?' + href;
        }

        window.location = url;
    });
    // 重置查询条件
    $("#rest-button").click(function () {
        window.location = url;
    });
    //导出相关
    $("#excel-button").click(function () {
        var downloadUrl = url + '?' + ((_obj == null || _obj.length == 0) ? 'action=excel' : _obj + '&action=excel');
        console.log(downloadUrl);
        window.open(downloadUrl, '_blank');
        return false;
    });

    // 3秒自动关闭提示
    function gAlert() {
        $(".alert").hide();
    }

    setTimeout(gAlert, 3000);

    //参数设置，若用默认值可以省略以下面代
    // toastr.options = {
    //     "closeButton": true, //是否显示关闭按钮
    //     "debug": false, //是否使用debug模式
    //     "positionClass": "toast-top-right",//弹出窗的位置
    //     "showDuration": "300",//显示的动画时间
    //     "hideDuration": "1000",//消失的动画时间
    //     "timeOut": "5000", //展现时间
    //     "extendedTimeOut": "1000",//加长展示时间
    //     "showEasing": "swing",//显示时的动画缓冲方式
    //     "hideEasing": "linear",//消失时的动画缓冲方式
    //     "showMethod": "fadeIn",//显示时的动画方式
    //     "hideMethod": "fadeOut" //消失时的动画方式
    // };

    //成功提示绑定
    $("#success").click(function () {
        toastr.success("祝贺你成功了");
    });

    //信息提示绑定
    $("#info").click(function () {
        toastr.info("这是一个提示信息");
    });

    //敬告提示绑定
    $("#warning").click(function () {
        toastr.warning("警告你别来烦我了");
    });

    //错语提示绑定
    $("#error").click(function () {
        toastr.error("出现错误，请更改");
    });

    //清除窗口绑定
    $("#clear").click(function () {
        toastr.clear();
    });

    //datetimepicker时间区间START
    $('.startTime').datetimepicker({
        locale: 'zh-CN',
        format: 'YYYY-MM-DD HH:mm:ss'
    });
    $('.endTime').datetimepicker({
        locale: 'zh-CN',
        format: 'YYYY-MM-DD HH:mm:ss',
        useCurrent: false
    });
    $(".startTime").on("dp.change", function (e) {
        if ($(this).parent().find(".endTime").length) {
            $('.endTime').eq(0).data("DateTimePicker").minDate(e.date);
        }
    });
    $(".endTime").on("dp.change", function (e) {
        if ($(this).parent().find(".startTime").length) {
            $('.startTime').eq(0).data("DateTimePicker").maxDate(e.date);
        }
    });
    $('.startTime1').datetimepicker({
        locale: 'zh-CN',
        format: 'YYYY-MM-DD HH:mm:ss'
    });
    $('.endTime1').datetimepicker({
        locale: 'zh-CN',
        format: 'YYYY-MM-DD HH:mm:ss',
        useCurrent: false
    });
    $(".startTime1").on("dp.change", function (e) {
        if ($(this).parent().find(".endTime1").length) {
            $('.endTime1').eq(0).data("DateTimePicker").minDate(e.date);
        }
    });
    $(".endTime1").on("dp.change", function (e) {
        if ($(this).parent().find(".startTime1").length) {
            $('.startTime1').eq(0).data("DateTimePicker").maxDate(e.date);
        }
    });
    $('.startTime2').datetimepicker({
        locale: 'zh-CN',
        format: 'YYYY-MM-DD HH:mm:ss'
    });
    $('.endTime2').datetimepicker({
        locale: 'zh-CN',
        format: 'YYYY-MM-DD HH:mm:ss',
        useCurrent: false
    });
    $(".startTime2").on("dp.change", function (e) {
        if ($(this).parent().find(".endTime2").length) {
            $('.endTime2').eq(0).data("DateTimePicker").minDate(e.date);
        }
    });
    $(".endTime2").on("dp.change", function (e) {
        if ($(this).parent().find(".startTime2").length) {
            $('.startTime2').eq(0).data("DateTimePicker").maxDate(e.date);
        }
    });
    //datetimepicker时间区间END
});

//时间格式化 useage:formatDate("1408609262000","yyyy-mm-dd");
function formatDate(timestamp, format) {
    if (typeof timestamp !== "undefined") {
        var date = new Date(Number(timestamp)),
            oDate = "",
            year = date.getFullYear(),
            month = date.getMonth() + 1,
            day = date.getDate(),
            hours = date.getHours(),
            minutes = date.getMinutes(),
            seconds = date.getSeconds();
        var time = [month, day, hours, minutes, seconds];
        for (i in time) {
            if (time[i] < 10) {
                time[i] = "0" + time[i];
            }
        }
        if (typeof format === "undefined") {
            oDate = year + "-" + time[0] + "-" + time[1];
            return oDate;
        } else {
            switch (format) {
                case "yyyy-mm-dd HH:mm:ss" :
                    oDate = year + "-" + time[0] + "-" + time[1] + " " + time[2] + ":"
                        + time[3] + ":" + time[4];
                    break;
                case "yyyy-mm-dd HH:mm" :
                    oDate = year + "-" + time[0] + "-" + time[1] + " " + time[2] + ":"
                        + time[3];
                    break;
                case "yyyy-mm-dd" :
                    oDate = year + "-" + time[0] + "-" + time[1];
                    break;
                case "yyyy-mm" :
                    oDate = year + "-" + time[0];
                    break;
                case "HH:mm:ss" :
                    oDate = time[2] + ":" + time[3] + ":" + time[4];
                    break;
                case "HH:mm" :
                    oDate = time[2] + ":" + time[3];
                    break;
                default :
                    break;
            }
            return oDate;
        }
    } else {
        throw new Error("formatDate():timestamp is not defined");
    }
}

/**
 * 秒转时分秒
 * @param    秒
 * @returns  result[],时、分、秒
 */
function formatSeconds(value) {
    var theTime = parseInt(value);// 秒
    var theTime1 = 0;// 分
    var theTime2 = 0;// 小时
    if (theTime >= 60) {
        theTime1 = parseInt(theTime / 60);
        theTime = parseInt(theTime % 60);
        if (theTime1 >= 60) {
            theTime2 = parseInt(theTime1 / 60);
            theTime1 = parseInt(theTime1 % 60);
        }
    }
    var result = [];
    result.push(theTime2);
    result.push(theTime1);
    result.push(theTime);
    return result;
}

/**
 * 比较两个时间大小
 * d1,d2为字符串,d1>d2 返回1;d1=d2 返回0;d1<d2 返回-1;
 */
function compareDate(d1, d2) {
    if ((new Date(d1.replace(/-/g, "\/"))) > (new Date(d2.replace(/-/g, "\/")))) {
        return 1;
    } else if ((new Date(d1.replace(/-/g, "\/"))) == (new Date(d2.replace(/-/g, "\/")))) {
        return 0;
    } else {
        return -1;
    }
}

/**
 * 格式化数字金额用逗号隔开保留小数
 * s:金额；n:小数位
 */
function formatMoney(s, n) {
    // n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
    var t = "";
    for (var i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return n > 0 ? (t.split("").reverse().join("") + "." + r) : t.split("").reverse().join("");
}

function loading(msg) {
    //获取浏览器页面可见高度和宽度
    var _PageHeight = document.documentElement.clientHeight,
        _PageWidth = document.documentElement.clientWidth;
    //计算loading框距离顶部和左部的距离（loading框的宽度为215px，高度为61px）
    var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,
        _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
    //在页面未加载完毕之前显示的loading Html自定义内容
    var _LoadingHtml = '<div id="loadingDiv" style="position:absolute;left:0;width:100%;height:' + _PageHeight + 'px;top:0;opacity:1;filter:alpha(opacity=80);z-index:10000;">' +
        '<div id="msg" style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 50px; padding-right: 5px; background: #FFF url(../../img/loading.gif) no-repeat scroll 5px 10px; border: 1px solid #ccc; color: #696969; ">请稍等. . .</div></div>';

    $("body").append(_LoadingHtml);
    if (msg) {
        $("#msg").html(msg);
    }
}

function loadingClose() {
    $("#loadingDiv").remove();
}

$("form").on("blur", "input.name", function () {
    var str = $(this).val();
    $(this).val(str.replace(/(^\s*)|(\s*$)/g, ""));
});