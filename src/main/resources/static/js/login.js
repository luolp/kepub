function showToast(msg, timeout = 2000) {
    $("#toast .weui-toast__content").html(msg);
    $("#toast").fadeIn(100);
    setTimeout(function () {
        $("#toast").fadeOut(100);
    }, timeout);
}
function check_input(th){
    if($("#submit_btn").html()=='登录中...'){
        return;
    }

    var username = $("input[name='username']").val();
    var password = $("input[name='password']").val();
    if('' == username) {showToast("请输入邮箱或可阅号"); return;}
    if('' == password) { showToast('请输入密码'); return;}
    var reqData = {"username":username,"password":hex_sha1(password)};

    $("#submit_btn").html('登录中...');

    $.ajax({
        type: "post",
        url: "/login",
        dataType: "json",
        data: reqData,
        success: function (result) {
            if(result.code == 0){
                location.href='/user/center';
            }else{
                showToast(result.msg);
                $("#submit_btn").html('登录');
            }
        },
        error:function(msg){
            showToast('服务器出现异常');
            $("#submit_btn").html('登录');
        }
    });
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