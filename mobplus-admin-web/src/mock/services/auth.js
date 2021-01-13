import Mock from 'mockjs2';
import { builder, getBody } from '../util';

// const username = ['admin', 'super'];
// 强硬要求 ant.design 相同密码
// '21232f297a57a5a743894a0e4a801fc3',
// const password = ['8914de686ab28dc22f30d3d8e107ff6c'];  admin, ant.design

const login = (options) => {
    const body = getBody(options);
    console.log('mock: body', body);
    //    if (!username.includes(body.username) || !password.includes(body.password)) {
    //        return builder(null, '账户或密码错误', 4001)
    //    }

    return builder('4291d7da9005377ec9aec4a71ea837f', 'ok', 2000);
};

const logout = () => {
    return builder({}, '[测试接口] 注销成功', 2000);
};

Mock.mock('/api/auth/login', 'post', login);
Mock.mock('/api/auth/logout', 'get', logout);

Mock.mock(/\/auth\/profile/, 'get', (options) => {
    return require('./data/profile.json');
});

Mock.mock('/api/auth/profile', 'put', () => {
    return builder('资料修改成功!', 'ok', 2000);
});
Mock.mock(/\/auth\/perms/, 'get', (options) => {
    return require('./data/permission.json');
});
Mock.mock(/\/auth\/menus/, 'get', (options) => {
    return require('./data/menu.json');
});

Mock.mock('/api/auth/passwd', 'post', () => {
    return builder('密码修改成功!', 'ok', 2000);
});
