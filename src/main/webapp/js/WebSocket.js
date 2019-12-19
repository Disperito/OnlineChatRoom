let websocket = null;
const host = document.location.host;

let userId = null;
let thisUserName = null;

function webSocketInit(userIdArg) {
    userId = userIdArg;
    if ('WebSocket' in window) {
        websocket = new WebSocket('ws://' + host + '/webSocket/' + userId);

        websocket.onerror = function () {
            alert("WebSocket连接发生错误");
        };

        websocket.onopen = function () {
            // alert("WebSocket连接成功")
        };

        websocket.onmessage = function (event) {
            const messageStr = event.data;
            const messageJSON = JSON.parse(messageStr);
            if (messageJSON.type === 'notify') {
                if (messageJSON['function'] === 'updateOnlineList') {
                    updateOnlineList(messageJSON.onlineUsers);
                }
            } else if ('message' === messageJSON.type) {
                receive(messageJSON);
            } else if ('record' === messageJSON.type) {
                console.log(messageJSON);
                renderRecentChatRecord(messageJSON);
            }
        };

        websocket.onclose = function () {
            // alert("WebSocket连接关闭");
        };


    } else {
        alert('Browser did not support websocket');
    }
}

window.onbeforeunload = function () {
    closeWebSocket();
};

function closeWebSocket() {
    websocket.close();
}

function sendMessage(content, target) {
    const message = {
        type: 'message',
        content: content,
        source: userId,
        sourceName: thisUserName,
        target: target
    };
    websocket.send(JSON.stringify(message));
}


function updateOnlineList(onlineUsers) {
    let id, name;
    const userList = $("#user_list");
    userList.empty();
    for (let key in onlineUsers) {
        id = key;
        name = onlineUsers[key];
        let newLine = "<li><a onclick=\"chooseChatTarget('" + id + "','" + name + "')\">" + name + "</a></li>";
        userList.append(newLine);
    }
}

function chooseChatTarget(id, name) {
    if (parseInt(id) === parseInt(userId)) {
        return;
    }
    chatTarget = id;
    chatTargetName = name;
    let private_chat_tip = "<div class=\"well private-chat-tip\">\n" +
        "<p>您当前正在和" + name + "私聊哦！<a onclick=\"switchToGroupChat()\">点此</a>恢复至群聊模式</p>\n" +
        "</div>";
    $("#main_well").append(private_chat_tip);
}

function switchToGroupChat() {
    chatTarget = "all";
    $(".private-chat-tip").remove();
}







