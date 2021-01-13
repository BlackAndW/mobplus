import ApiAuth from '@/api/auth';
import { constantRouterMap } from '@/config/router.config';
import { generatorRouter } from '@/router/dynamic-router';

const permission = {
    state: {
        routers: constantRouterMap,
        addRouters: []
    },
    mutations: {
        SET_ROUTERS: (state, routers) => {
            state.addRouters = routers;
            state.routers = constantRouterMap.concat(routers);
        }
    },
    actions: {
        GenerateRoutes ({ commit }, data) {
            return new Promise((resolve, reject) => {
                ApiAuth.getMenu().then(data => {
                    const routers = generatorRouter(data);
                    commit('SET_ROUTERS', routers);
                    resolve();
                }).catch(err => {
                    reject(err);
                });
            });
        }
    }
};

export default permission;
