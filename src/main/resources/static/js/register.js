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
function check_input(){
    if($("#submit_btn").html()=='注册中...'){
        return;
    }

    var email = $("input[name='email']").val();
    var password = $("input[name='password']").val();
    var nickname = $("input[name='nickname']").val();
    if('' == email) {showToast("请输入邮箱"); return;}
    if('' == password) { showToast('请输入密码'); return;}
    if('' == nickname) { showToast('请输入昵称'); return;}
    var reqData = {"email":email,"password":password,"nickname":nickname};

    $("#submit_btn").html('注册中...');
    $.ajax({
        type: "post",
        url:"./register",
        dataType: "json",
        data: reqData,
        success: function(result) {
            if(result.code == 0){
                $(".content").hide();
                $("#successMsg").show();
            }else{
                showFailAlert("注册失败", result.msg);
            }
        },
        error:function(msg){
            showToast('服务器出现异常');
        },
        complete:function(data){
            $("#submit_btn").html('注册');
        }
    });

    return false;
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