module.exports = (req, res, next) => {
    if (req.path === '/login') {
        return login(req, res);
    }
    else if (req.path === '/dataList') {
        return dataList(res);
    }
    else if (req.path === '/organizationCode') {
        return organizationCode(res);
    }
    else if (req.path === '/user/resetPwd') {
        return resetPassword(res);
    }
    next();
}

const login = (req, res) => {
    if (req.body.userId === 'admin' && req.body.password === 'admin') {
        res.status(200).json({
            "data": {
                "userId": "admin",
                "userName": "admin",
                "adminnistratorFlag": "2"
            },
            "success": true
        })
        return res;
    }
    else if (req.body.userId === 'user' && req.body.password === 'user') {
        res.status(200).json({
            "data": {
                "userId": "user",
                "userName": "user",
                "adminnistratorFlag": "1"
            },
            "success": true
        })
        return res;
    }
    else {
        res.status(200).json({
            "success": false,
            "errMsg": "Message0001"
        })
        return res;
    }
};

const dataList = (res) => {
    const newData = [];
    for (let i = 0; i < 100; i++) {
        newData.push({
            key: i.toString(),
            name: `Edrward ${i}`,
            age: 32,
            address: `London Park no. ${i}`,
        });
    }
    res.status(200).json({
        "data": newData,
        "success": true
    })
    return res;
};

const organizationCode = (res) => {
    const options = [
        {value: '001',label: '组织001',pid: ''},
        {value: 'all',label: '所有部门',pid: '001'},
        {value: '101',label: '部门001',pid: '001'},
        {value: 'all',label: '所有Tower',pid: '101'},
        {value: '201',label: 'Tower001',pid: '101'},
        {value: '002',label: '组织002',pid: ''},
        {value: 'all',label: '所有部门',pid: '002'},
        {value: '102',label: '部门002',pid: '002'},
        {value: 'all',label: '所有Tower',pid: '102'},
        {value: '202',label: 'Tower002',pid: '102'}
    ];
    res.status(200).json({
        "data": options,
        "success": true
    })
    return res;
};

const resetPassword = (res) => {
    res.status(200).json({
        "success": true
    })
    return res;
};