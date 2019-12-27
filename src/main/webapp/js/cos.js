var Bucket = 'dmwqaq-1300596096';
var Region = 'ap-shanghai';     /* 存储桶所在地域，必须字段 */


// 初始化实例
var cos = new COS({
    getAuthorization: function (options, callback) {
        // 异步获取临时密钥
        /*$.ajax({
            url: '/cos/get',
            type: 'GET',
            data: {
                bucket: Bucket,
                region: Region,
            },
            success: function (data) {
                const credentials = data.credentials;
                TmpSecretId = credentials['tmpSecretId'];
                TmpSecretKey = credentials['tmpSecretKey'];
                XCosSecurityToken = credentials['sessionToken'];
                ExpiredTime = data['expiredTime']
            }
        });*/
        $.get('/cos/get', {
            bucket: options.Bucket,
            region: options.Region,
        }, function (data) {
            data = JSON.parse(data);
            const credentials = data.credentials;
            console.log(credentials);
            callback({
                TmpSecretId: credentials['tmpSecretId'],
                TmpSecretKey: credentials['tmpSecretKey'],
                XCosSecurityToken: credentials['sessionToken'],
                ExpiredTime: data['expiredTime']
            });
        });
    }
});
