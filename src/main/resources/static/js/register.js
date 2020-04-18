function check_input(){
    if($("#submit_btn").html()=='注册中...'){
        return;
    }
    $("#submit_btn").html('注册中...');
    $.ajax({
        type: "post",
        url:"register.php",
        dataType: "json",
        data: $("#reg_form").serialize(),
        success: function(data) {
            my.msg(data.msg);
            if(data.url!=null){
                location.href=data.url;
            }
            $("#submit_btn").html('注册');
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