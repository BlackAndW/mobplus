const PERMISSION_ENUM = {
    'create': { key: 'create', label: '新增' },
    'delete': { key: 'delete', label: '删除' },
    'edit': { key: 'edit', label: '修改' },
    'query': { key: 'query', label: '查询' },
    'get': { key: 'get', label: '详情' },
    'enable': { key: 'enable', label: '启用' },
    'disable': { key: 'disable', label: '禁用' },
    'import': { key: 'import', label: '导入' },
    'export': { key: 'export', label: '导出' }
};

const plugin = {
    vm: {},
    // eslint-disable-next-line no-unused-vars
    install (Vue) {
        if (this.installed) {
            return;
        }
        this.installed = true;
        Object.defineProperties(Vue.prototype, {
            $auth: {
                get () {
                    const _this = this;
                    return (permissions) => {
                        const [permission, action] = permissions.split('.');
                        const permissionList = _this.$store.getters.perms;
                        return permissionList.find((val) => {
                            return val.permissionId === permission;
                        }).actionList.findIndex((val) => {
                            return val === action;
                        }) > -1;
                    };
                }
            },
            $enum: {
                get () {
                    // const _this = this;
                    return (val) => {
                        let result = PERMISSION_ENUM;
                        val && val.split('.').forEach(v => {
                            result = result && result[v] || null;
                        });
                        return result;
                    };
                }
            },
            $hasPermissions: {
                get () {
                    const _this = this;
                    return (permissions) => {
                        const permissionList = _this.$store.getters.perms;
                        let hasPermission = false;
                        for (let idx = 0; idx < permissions.length; idx++) {
                            if (permissionList.includes(permissions[idx])) {
                                hasPermission = true;
                                break;
                            }
                        }
                        return hasPermission;
                    };
                }
            },
            $hasRoles: {
                get () {
                    const _this = this;
                    return (roles) => {
                        const roleList = _this.$store.getters.roles;
                        let hasRole = false;
                        for (let idx = 0; idx < roles.length; idx++) {
                            if (roleList.includes(roles[idx])) {
                                hasRole = true;
                                break;
                            }
                        }
                        return hasRole;
                    };
                }
            }
        });
    }
};

export default plugin;
