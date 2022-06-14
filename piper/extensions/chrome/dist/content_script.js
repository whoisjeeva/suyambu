var wakeup = function() {
    setTimeout(function(){
        chrome.runtime.sendMessage('ping', function(){
            console.log("pong");
        });
        wakeup();
    }, 5000);
}
wakeup();