let chatTarget = "all";
let chatTargetName;

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
        type: 'POST',
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


// function dev_updateOnlineList() {
//     let id = $("#dev_user_id").val();
//     let name = $("#dev_user_name").val();
//     const userList = $("#user_list");
//     let newLine = "<li><a onclick=\"chooseChatTarget('" + id + "','" + name + "')\">" + name + "</a></li>";
//     userList.append(newLine);
// }