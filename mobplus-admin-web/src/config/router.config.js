// eslint-disable-next-line no-unused-vars
import { UserLayout, BlankLayout } from '@/layouts';

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
    {
        path: '/auth',
        component: UserLayout,
        redirect: '/auth/login',
        hidden: true,
        children: [
            {
                path: 'login',
                name: 'login',
                component: () => import('@/views/auth/login')
            }
        ]
    },
    {
        path: '/404',
        component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
    }
];
