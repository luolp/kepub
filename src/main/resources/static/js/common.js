var my = {
    // 提示消息弹出框 msg:要显示的消息 time:显示的时长
    msg:function(msg,time){
        time=time||2000;
        if(!$('#msg').length>0){
            var tpl='<div id="msg" style="position: fixed;top: 0;bottom: 0;left:0;right:0;" hidden>'
                +'<div style="height:42px;width:100%;position:absolute;margin-top: -21px;top: 50%;line-height: 42px;text-align: center;">'
                +'<span style="padding: 10px 20px;background-color: rgba(0,0,0,.6);border-radius: 2px;color: #fff;">'
                +msg
                +'</span></div>';
            $('body').append(tpl);
        }
        $("#msg").fadeIn(600,function(){
            setTimeout('$("#msg").fadeOut(600)',time);
        });
    }
}

