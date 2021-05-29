const contentInput = document.getElementById("content");

contentInput.addEventListener("keyup", function (event) {
    if (event.code === 13) {
        event.preventDefault();
        document.getElementById("sendMessage").click();
    }
});


//part to reinit the message every 5 second
var auto_refresh = setInterval(
function()
    {
        $('#conversation').load('conversation_detail.html #update');
        }
    },5000);
//Load the file containing the chat log
function loadLog(){
    var oldscrollHeight = $("#conversation").attr("scrollHeight") - 20; //Scroll height before the request
    $.ajax({//Auto-scroll
            var newscrollHeight = $("#conversation").attr("scrollHeight") - 20; //Scroll height after the request
            if(newscrollHeight > oldscrollHeight){
                $("#conversation").animate({ scrollTop: newscrollHeight }, 'normal'); //Autoscroll to bottom of div
            }
        },
    });
auto_refresh;
loadLog();