function check_input(th){
    if($("#submit_btn").html()=='登录中...'){
        return;
    }
    $("#submit_btn").html('登录中...');

    var password=document.querySelector("#password2").value;
    var username=document.querySelector("#username").value;
    // 不让输入框值的长度发生变化,没什么特殊作用
    // document.querySelector("#password2").value=hex_sha1(password).substring(0,password.length);
    password=hex_sha1(password+password);

    $.ajax({
        type: "post",
        url: "login.php",
        dataType: "json",
        data: {username:username,
            password:password
        },
        success: function (data) {
            if (data.msg != null) {
                my.msg(data.msg);
            }
            if (data.url != null) {
                location.href = data.url;
            }
            $("#submit_btn").html('登录');
        },
        complete: function (data) {
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