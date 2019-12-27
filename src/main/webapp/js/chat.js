function send() {
    let isPrivateChat = chatTarget !== 'all';

    const messageContainer = $(".message-container");
    const textInput = $("#input_area");
    const text = textInput.val();
    if (!text) {
        alert('请输入内容');
        return;
    }
    const newDate = new Date();
    const datetime = newDate.toLocaleDateString() + ' ' + newDate.toLocaleTimeString();

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

function updateScroll() {
    let message = document.getElementById('dialog_well');
    message.scrollTop = message.scrollHeight;
}

function receive(messageJSON) {
    if (messageJSON.source === userId) {
        return;
    }

    // let array = for_dialog_well.record;
    // array[array.length] = messageJSON;
    for_dialog_well.record.push(messageJSON);
    /*const messageContainer = $(".message-container");

    let text = messageJSON['content'];

    // 文件类型的消息
    if (text.indexOf('dmwqaq-1300596096.cos.ap-shanghai.myqcloud.com') !== -1) {
        let fileName = decodeURI(text.substring(47, text.lastIndexOf('-')).replace('-', '.'));

        console.log("dmwqaq-1300596096.cos.ap-shanghai.myqcloud.com".length);
        text = "<a href=\"http://" + text + "\">" + fileName + "</a>"
    }
    // const text = messageJSON.content;
    const newDate = new Date();
    const datetime = newDate.toLocaleDateString() + ' ' + newDate.toLocaleTimeString();
    const sourceName = messageJSON.sourceName;
    const isPrivateChat = messageJSON.target !== 'all';

    const sourceDOM = $("<div></div>").addClass("message-source").text(
        (isPrivateChat ? '[私聊]  ' : '') +
        sourceName + '：');
    const contentDOM = $("<div></div>").addClass("message-content").html(text);
    contentDOM.html(contentDOM.html().replace(/\n/g, '<br/>'));
    const datetimeDOM = $("<div></div>").addClass("message-datetime").text(datetime);
    const messageDOM = $("<div></div>").addClass("message").addClass("message-that");

    messageDOM.append(sourceDOM);
    messageDOM.append(contentDOM);
    messageDOM.append(datetimeDOM);
    messageContainer.append(messageDOM);*/
    // updateScroll();
    setTimeout(updateScroll, 300);
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

