import { useNavigate } from "react-router-dom";

function request(method, url, body) {
    method = method.toUpperCase();
    if (method === 'GET') {
        // fetch的GET不允许有body，参数只能放在url中
        body = undefined;
    } else {
        body = body && JSON.stringify(body);
    }

    if(url.includes("/login")
    || url.includes("/user/resetPwd")
    || url.includes("/userManagementSearch")
    || url.includes("/userManagementSave")
    || url.includes("/userManagementDelete")){
        url = process.env.REACT_APP_BASE_URL + url;
    }else{
        url = process.env.REACT_APP_BASE_URL2 + url;
    }

    return fetch(url, {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Access-Token': localStorage.getItem('access_token') || '', // 从localStorage中获取access token
            'Authorization': localStorage.getItem('access_token')
        },
        body
    })
        .then((res) => {
            if (res.status === 401) {
                useNavigate('/Login')
            } else {
                const token = res.headers.get('access-token');
                if (token) {
                    localStorage.setItem('access_token', token);
                }
                return res.json();
            }
        })
        .catch(e => {
            alert('通信失败');
        });
}

export const get = url => request('GET', url);
export const post = (url, body) => request('POST', url, body);
export const put = (url, body) => request('PUT', url, body);
export const del = (url, body) => request('DELETE', url, body);