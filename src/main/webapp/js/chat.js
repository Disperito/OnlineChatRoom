function send() {
    let isPrivateChat = chatTarget !== 'all';

    const messageContainer = $(".message-container");
    const textInput = $("#input_area");
    const text = textInput.val();
    if (!text) {
        alert('请输入内容');
        return;
    }
    const datetime = dateFormat(new Date());

    const sourceDOM = $("<div></div>").addClass("message-source").text(
        (isPrivateChat ? '[私聊 -> ' + chatTargetName + ']  ' : '') +
        thisUserName + '：');
    const contentDOM = $("<div></div>").addClass("message-content").text(text);
    contentDOM.html(contentDOM.html().replace(/\n/g, '<br/>'));
    const datetimeDOM = $("<div></div>").addClass("message-datetime").text(datetime);
    const messageDOM = $("<div></div>").addClass("message").addClass("message-this");

    messageDOM.append(sourceDOM);
    messageDOM.append(contentDOM);
    messageDOM.append(datetimeDOM);
    messageContainer.append(messageDOM);
    updateScroll();
    textInput.val('');
    sendMessage(text, chatTarget);
}

function dateFormat(date) {
    const yyyy = date.getFullYear();
    const MM = date.getMonth() + 1;
    const dd = date.getDate();
    const HH = date.getHours()
    const mm = date.getMinutes();
    const ss = date.getSeconds();
    return yyyy + '-' + MM + '-' + dd + '  ' + HH + ':' + mm + ':' + ss;
}

function updateScroll() {
    const message = document.getElementById('dialog_well');
    message.scrollTop = message.scrollHeight;
}

function receive(messageJSON) {
    if (messageJSON.source === userId) {
        return;
    }

    const messageContainer = $(".message-container");
    const text = messageJSON.content;
    const datetime = dateFormat(new Date());
    const sourceName = messageJSON.sourceName;
    const isPrivateChat = messageJSON.target !== 'all';

    const sourceDOM = $("<div></div>").addClass("message-source").text(
        (isPrivateChat ? '[私聊]  ' : '') +
        sourceName + '：');
    const contentDOM = $("<div></div>").addClass("message-content").text(text);
    contentDOM.html(contentDOM.html().replace(/\n/g, '<br/>'));
    const datetimeDOM = $("<div></div>").addClass("message-datetime").text(datetime);
    const messageDOM = $("<div></div>").addClass("message").addClass("message-that");

    messageDOM.append(sourceDOM);
    messageDOM.append(contentDOM);
    messageDOM.append(datetimeDOM);
    messageContainer.append(messageDOM);
    updateScroll();
}

/*
function testReceive() {
    const messageContainer = $(".message-container");
    const datetime = dateFormat(new Date());
    const text = "    const messageContainer = $(\".message-container\");\n    const messageContainer = $(\".message-c" +
        "ontainer\");\n    const messageContainer = $(\".message-container\");\n    const messageContainer = $(\".mess" +
        "age-container\");\n";

    const newMessage = "<div class=\"message message-that\">\n" +
        "<div class=\"message-content\">" + text + "</div>\n" +
        "<div class=\"message-datetime\">" + datetime + "</div></div>";
    messageContainer.append(newMessage);
    updateScroll();
}*/

