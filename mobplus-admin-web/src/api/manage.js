import http from '@/utils/request';

const api = {
    user: '/user',
    role: '/role',
    service: '/service',
    permission: '/permission',
    permissionNoPager: '/permission/no-pager',
    orgTree: '/org/tree'
};

export default api;

export function getUserList (parameter) {
    return http.get(api.user,
        parameter
    );
}

export function getRoleList (parameter) {
    return http.get(api.role,
        parameter
    );
}

export function getServiceList (parameter) {
    return http.get(api.service,
        parameter
    );
}

export function getPermissions (parameter) {
    return http.get(api.permissionNoPager, parameter);
}

export function getOrgTree (parameter) {
    return http.get(api.orgTree, parameter);
}

// id == 0 add     post
// id != 0 update  put
export function saveService (parameter) {
    return http.put(api.service, parameter);
}
