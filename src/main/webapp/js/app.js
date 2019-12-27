let chatTarget = "all";
let chatTargetName;
let for_dialog_well;


function login() {
    const idInput = $("#id_input");
    const passwordInput = $("#password_input");
    const id = idInput.val();
    const password = passwordInput.val();

    if (!loginValidate(id, password)) {
        return;
    }
    $.ajax({
        url: 'user/login',
        type: 'POST',
        data: {
            id: id,
            password: password
        },
        success: function (responseText) {
            if ('false' === responseText) {
                alert('登录失败');
            } else if ('true' === responseText) {
                location.reload();
            }
        }
    });
}

function register() {
    const username = $("#register_name").val();
    const password = $("#register_password").val();

    if (!username) {
        alert('请输入用户名！');
        return;
    }
    if (!password) {
        alert('请输入密码！');
        return;
    }

    $.ajax({
        url: '/user/register',
        type: 'POST',
        data: {
            username: username,
            password: password
        },
        success: function (response_text) {
            $("#register_modal").modal('hide');
            $("#id_input").val(response_text.replace('注册成功，您的ID是', ''));
            alert(response_text);
        }
    });
}

function loginValidate(id, password) {
    if (!id) {
        alert('ID不能为空！');
        $("#id_input").focus();
        return false;
    }

    if (!password) {
        alert('密码不能为空！');
        $("#password_input").focus();
        return false;
    }
    return true;
}

function logout() {
    $.ajax({
        url: 'user/logout',
        type: 'delete',
        success: function (responseText) {
            if ('true' === responseText) {
                location.reload();
            } else {
                alert('登出失败！');
            }
        }
    });
}

function afterLogin(userName) {
    if (userName === null || userName === undefined || /^[ ]*$/.test(userName)) {
        return;
    }
    setAfterLoginUI(userName);

    for_dialog_well = new Vue({
        el: '#dialog_well',
        data: {
            record: null
        },
        methods: {
            messageClass: function (message) {
                if (message['sourceName'] === thisUserName) {
                    return 'message message-this'
                } else {
                    return 'message message-that'
                }
            }
        },
        computed: {}
    });
}

function setAfterLoginUI(userName) {
    thisUserName = userName;
    let logoutLi = "<li><a role=\"menuitem\" onclick=\"logout()\">登出</a></li>";
    $("#right-dropdown").prepend(logoutLi);

    $("<li></li>").addClass("navbar-text")
        .attr('id', 'welcome')
        .text("欢迎您，" + userName)
        .prependTo($("ul.navbar-right"));

}

function renderRecentChatRecord(messageJSON) {
    const messages = messageJSON['messages'];
    for (let i = 0; i < messages.length; i++) {
        let message = messages[i];
        const date = new Date(message.createTime);
        message['createTime'] = date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
        if (message['target'] !== 'all') {
            message['sourceName'] = '[私聊] ' + message['sourceName'];
        }

        // 文件类型的消息
        const content = message['content'];
        if (content.indexOf('dmwqaq-1300596096.cos.ap-shanghai.myqcloud.com') !== -1) {
            let fileName = decodeURI(content.substring(47, content.lastIndexOf('-')).replace('-', '.'));

            console.log("dmwqaq-1300596096.cos.ap-shanghai.myqcloud.com".length);
            message['content'] = "<a href=\"http://" + content + "\">" + fileName + "</a>"
        }

    }
    for_dialog_well.record = messageJSON['messages'];
    setTimeout(updateScroll, 1000);
}

function uploadFile() {
    const file = $("#file")[0].files[0];
    let key = (file['name']).replace('.', '-') + '-' + new Date().getTime();
    cos.putObject({
        Bucket: Bucket,
        Region: Region,
        Key: key,
        StorageClass: 'STANDARD',
        Body: file,
        onProgress: function (progressData) {
            console.log(JSON.stringify(progressData));
        }
    }, function (err, data) {
        console.log(err || data);
        const location = data['Location'];
        sendMessage(location, chatTarget);

        let fileName = decodeURI(location.substring(47, location.lastIndexOf('-')).replace('-', '.'));
        const text = "<a href=\"http://" + location + "\">" + fileName + "</a>";

        let isPrivateChat = chatTarget !== 'all';
        const messageContainer = $(".message-container");
        const newDate = new Date();
        const datetime = newDate.toLocaleDateString() + ' ' + newDate.toLocaleTimeString();

        const sourceDOM = $("<div></div>").addClass("message-source").text(
            (isPrivateChat ? '[私聊 -> ' + chatTargetName + ']  ' : '') +
            thisUserName + '：');
        const contentDOM = $("<div></div>").addClass("message-content").html(text);
        contentDOM.html(contentDOM.html().replace(/\n/g, '<br/>'));
        const datetimeDOM = $("<div></div>").addClass("message-datetime").text(datetime);
        const messageDOM = $("<div></div>").addClass("message").addClass("message-this");

        messageDOM.append(sourceDOM);
        messageDOM.append(contentDOM);
        messageDOM.append(datetimeDOM);
        messageContainer.append(messageDOM);
        updateScroll();
    });
}


$("#register_modal").on('hide.bs.modal', function () {
    $("#register_name").val('');
    $("#register_password").val('');
});

