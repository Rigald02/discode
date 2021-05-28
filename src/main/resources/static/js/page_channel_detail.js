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
        $('#chatbox').load('channel_detail.html #update');
        console.log("salut");
        }
    },5000);
//Load the file containing the chat log
function loadLog(){
    var oldscrollHeight = $("#chatbox").attr("scrollHeight") - 20; //Scroll height before the request
    console.log("les"");
    $.ajax({//Auto-scroll
            var newscrollHeight = $("#chatbox").attr("scrollHeight") - 20; //Scroll height after the request
            if(newscrollHeight > oldscrollHeight){
                $("#chatbox").animate({ scrollTop: newscrollHeight }, 'normal'); //Autoscroll to bottom of div
                console.log("zoulou");
            }
        },
    });