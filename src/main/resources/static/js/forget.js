function showToast(msg, timeout = 2000) {
    $("#toast .weui-toast__content").html(msg);
    $("#toast").fadeIn(100);
    setTimeout(function () {
        $("#toast").fadeOut(100);
    }, timeout);
}
function showFailAlert(title, msg) {
    $("#failAlert .weui-dialog__title").html(title);
    $("#failAlert .weui-dialog__bd").html(msg);
    $("#failAlert").show();
}
$("#failAlert a").click(function () {
    $('#failAlert').hide();
});

var step=1;
function next_step(){
    if(step==1){
        if($("#submit_btn").html()=='稍等...'){
            return;
        }

        var username = $("input[name='username']").val();
        if('' == username) {showToast("请输入邮箱或可阅号"); return;}
        var reqData = {"username":username};

        $("#submit_btn").html('稍等...');

        $.ajax({
            url: "/forget/getCaptcha",
            dataType: "json",
            data: reqData,
            success: function (result) {
                if(result.code == 0){
                    $('.input').hide();
                    $('#codeInput input').attr('placeholder', '请输入验证码（'+ result.msg + '）');
                    $('#codeInput').show();
                    step=2;
                }else{
                    showToast(result.msg);
                }
            },
            error:function(msg){
                showToast('服务器出现异常');
            },
            complete: function (data) {
                $("#submit_btn").html('下一步');
            }
        });
    }else if(step==2){
        if($("input[name='code']").val()==''){
            my.msg('请输入验证码');
        }else{
            $('.input').hide();
            $('#pwdInput').show();
            $("#submit_btn").html('提交');
            step=3;
        }
    }else if(step==3){
        if($("#submit_btn").html()=='稍等...'){
            return;
        }

        var username = $("input[name='username']").val();
        var code = $("input[name='code']").val();
        var password = $("input[name='password']").val();
        if('' == password) { showToast('请输入密码'); return;}
        var reqData = {"username":username,"code":code,"password":password};

        $("#submit_btn").html('稍等...');
        $.ajax({
            type: "post",
            url: "/forget/updatePwd",
            dataType: "json",
            data: reqData,
            success: function(result) {
                if(result.code == 0){
                    $(".title").hide();
                    $(".content").hide();
                    $("#successMsg").show();
                }else{
                    showFailAlert("修改失败", result.msg);
                }
            },
            error:function(msg){
                showToast('服务器出现异常');
            },
            complete:function(data){
                $("#submit_btn").html('提交');
            }
        });
    }
}
$(function(){
    $(".opt").click(function(){
        if($(this).attr("class")=='opt eye-close'){
            $(this).prev().attr("type",'text');
            $(this).removeClass("eye-close");
            $(this).addClass("eye-open");
        }else{
            $(this).prev().attr("type",'password');
            $(this).removeClass("eye-open");
            $(this).addClass("eye-close");
        }
    });
});