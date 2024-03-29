import Vue from 'vue';
import store from '@/store';

/**
 * Action 权限指令
 * 指令用法：
 *  - 在需要控制 action 级别权限的组件上使用 v-action:[method] , 如下：
 *    <i-button v-action:add >添加用户</a-button>
 *    <a-button v-action:delete>删除用户</a-button>
 *    <a v-action:edit @click="edit(record)">修改</a>
 *
 *  - 当前用户没有权限时，组件上使用了该指令则会被隐藏
 *  - 当后台权限跟 pro 提供的模式不同时，只需要针对这里的权限过滤进行修改即可
 *
 *  @see https://github.com/sendya/ant-design-pro-vue/pull/53
 */
const action = Vue.directive('role', {
    inserted: function (el, binding, vnode) {
        const { value } = binding;
        if (value && value instanceof Array && value.length > 0) {
            const roleList = store.getters.roles;
            let hasRole = false;
            for (let idx = 0; idx < value.length; idx++) {
                if (roleList.includes(value[idx])) {
                    hasRole = true;
                    break;
                }
            }
            if (!hasRole) {
                el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none');
            }
        }
    }
});

export default action;
