import Mock from 'mockjs2';
import { builder, getQueryParameters, PageWrapper } from '../util';

Mock.mock(/\/sys\/depart\/tree/, 'get', (options) => {
    const ds = [];
    for (let idx = 1; idx < 5; idx++) {
        const item = {
            id: idx,
            code: 'A200_' + idx,
            name: 'John Level1'
        };
        item.children = [];
        for (let idj = 1; idj < 15; idj++) {
            const item1 = {
                id: idj + idx * 100,
                code: 'A200' + idx + '0_' + idj,
                name: 'A200' + idx + '0_' + idj
            };
            item.children.push(item1);
        }
        ds.push(item);
    }
    return builder(ds, 'ok', 2000);
});
Mock.mock(/\/sys\/depart/, 'get', (options) => {
    const parameters = getQueryParameters(options);
    const pageNo = parseInt(parameters.pageNo);
    const pageSize = parseInt(parameters.pageSize);
    console.log('==================  pageSize ' + pageSize);
    const ds = [];
    for (let idx = 0; idx < pageSize; idx++) {
        const item = {
            id: Mock.mock('@guid'),
            code: 'A200_' + idx,
            name: 'John Level1',
            leader: '三本56',
            mobile: '13098121912',
            fax: '0755-091821',
            address: 'New York No. 1 Lake Park'
        };
        item.children = [];
        for (let idj = 100; idj < 110; idj++) {
            const item1 = {
                id: Mock.mock('@guid'),
                code: 'A200' + idx + '0_' + idj,
                name: 'John Level1',
                leader: '三本56',
                mobile: '13098121912',
                fax: '0755-091821',
                address: 'New York No. 1 Lake Park'
            };
            item.children.push(item1);
        }
        ds.push(item);
    }
    const body = PageWrapper(pageSize, pageNo, 100, ds);
    return builder(body, 'ok', 2000);
});

Mock.mock(/\/sys\/role\/perm/, 'get', (options) => {
    return require('./data/sys-menu-perms.json');
});
Mock.mock(/\/sys\/role/, 'put', (options) => {
    return builder(null, 'ok', 2000);
});

Mock.mock(/\/sys\/role\/sct/, 'get', (options) => {
    const ds = [];
    for (let idx = 0; idx < 10; idx++) {
        const item = {
            id: idx,
            code: 'A200_' + idx,
            name: Mock.mock('@name')
        };
        ds.push(item);
    }
    return builder(ds, 'ok', 2000);
});

Mock.mock(/\/sys\/role/, 'get', (options) => {
    const parameters = getQueryParameters(options);
    const pageNo = parseInt(parameters.pageNo);
    const pageSize = parseInt(parameters.pageSize);
    const ds = [];
    for (let idx = 0; idx < pageSize; idx++) {
        const item = {
            id: Mock.mock('@guid'),
            code: 'A200_' + idx,
            name: Mock.mock('@name'),
            status: 1,
            sort: 1,
            memo: 'ModeModeModeMemoMemoMemo',
            creatorName: Mock.mock('@name'),
            createAt: '2019-09-06 08:12:12',
            updatorName: Mock.mock('@name'),
            updateAt: '2019-09-06 08:12:12'
        };
        ds.push(item);
    }
    const body = PageWrapper(pageSize, pageNo, 100, ds);
    return builder(body, 'ok', 2000);
});

Mock.mock(/\/sys\/user/, 'put', (options) => {
    return builder(null, 'ok', 2000);
});
/* =================================== */
Mock.mock(/\/sys\/user/, 'get', (options) => {
    const parameters = getQueryParameters(options);
    const pageNo = parseInt(parameters.pageNo);
    const pageSize = parseInt(parameters.pageSize);
    console.log('==================  pageSize ' + pageSize);
    const ds = [];
    for (let idx = 0; idx < pageSize; idx++) {
        const item = {
            id: Mock.mock('@guid'),
            account: 'A200_' + idx,
            nickName: 'John Level1',
            name: '三本56',
            avatar: 'https://randomuser.me/api/portraits/women/6.jpg',
            gender: 1,
            mobile: '13098121912',
            birthday: '2019-01-22',
            status: 2,
            memo: 'ModeModeModeMemoMemoMemo',
            dptName: '广州中心',
            dptId: '112',
            creatorName: Mock.mock('@name'),
            createAt: '2019-09-06 08:12:12',
            roles: [1, 2, 3],
            updatorName: Mock.mock('@name'),
            updateAt: '2019-09-06 08:12:12'
        };
        ds.push(item);
    }
    const body = PageWrapper(pageSize, pageNo, 100, ds);
    return builder(body, 'ok', 2000);
});

Mock.mock(/\/sys\/dict\/1001\/item/, 'get', (options) => {
    return require('./data/sys-datdict-item.json');
});
Mock.mock(/\/sys\/dict/, 'get', (options) => {
    return require('./data/sys-datadict-1011.json');
});

/* ========================================================== */
Mock.mock(/\/sys\/categories\/1002\/item/, 'get', (options) => {
    return require('./data/sys-categories-1002.json');
});

Mock.mock(/\/sys\/categories/, 'get', (options) => {
    return require('./data/sys-categories.json');
});

Mock.mock(/\/sys\/perm/, 'get', (options) => {
    return require('./data/sys-perm.json');
});
Mock.mock(/\/sys\/menu\/perm/, 'get', (options) => {
    return require('./data/sys-menu-perms.json');
});

Mock.mock(/\/sys\/menu/, 'get', (options) => {
    return require('./data/sys-menu.json');
});
