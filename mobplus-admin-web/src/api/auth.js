import http from '@/utils/request';

export default {
    login: function (params) {
        return http.post('/auth/login', params);
    },
    logout: function () {
        return http.get('/auth/logout');
    },
    profile: function () {
        return http.get('/auth/profile');
    },
    permission: function () {
        return http.get('/auth/perms');
    },
    getMenu: function () {
        return http.get('/auth/menus');
    }

    /* ====================================== */
    // updateCurrentPasswd: function(params){
    //     return http.post('/auth/passwd', params);
    // },
    // updateCurrentUserInfo: function(params){
    //     return http.put('/auth/profile', params);
    // }
};
