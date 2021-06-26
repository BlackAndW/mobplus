// /*UserLayout, */
import { BasicLayout, RouteView, BlankLayout, PageView } from '@/layouts';

// 前端路由表
const constantRouterComponents = {
    // 基础页面 layout 必须引入
    BasicLayout: BasicLayout,
    BlankLayout: BlankLayout,
    RouteView: RouteView,
    PageView: PageView,

    // 你需要动态引入的页面组件
    Dashboard: () => import('@/views/dashboard/dashboard'),
    // Workplace: () => import('@/views/dashboard/Workplace'),
    // Monitor: () => import('@/views/dashboard/Monitor'),
    // ...more

    // 系统模块
    SysConfList: () => import('@/views/sys/conf-list'),
    SysCategoryList: () => import('@/views/sys/category-list'),
    SysPermissionList: () => import('@/views/sys/permission-list'),
    SysMenuList: () => import('@/views/sys/menu-list'),

    SysRoleList: () => import('@/views/sys/role-list'),
    SysUserList: () => import('@/views/sys/user-list'),
    SysAuditLogList: () => import('@/views/sys/auditlog-list'),

    // 广告管理
    AdvPlatformList: () => import('@/views/ad/adv-list'),
    AdPositionList: () => import('@/views/ad/adPosition-list'),

    // 内容管理
    GameList: () => import('@/views/cms/game-list'),
    TalkCategoryList: () => import('@/views/cms/talk-category-list'),
    TalkList: () => import('@/views/cms/talk-list'),

    // 发行管理
    ChannelList: () => import('@/views/release/channel-list'),

    // 应用管理
    AppList: () => import('@/views/app/app-list'),
    AppVersionList: () => import('@/views/app/appVersion-list'),
    AppProjectList: () => import('@/views/app/appProject-list'),
    AppConfigList: () => import('@/views/app/appConfig-list'),
    AppPositionList: () => import('@/views/app/appPosition-list'),
    AppAdvertiserList: () => import('@/views/app/appAdvertiser-list'),
    AppMobileConfList: () => import('@/views/app/appMobileConf-list'),
    AppActivity: () => import('@/views/app/appActivity-list'),
    AppFunction: () => import('@/views/app/appFunction-list'),

    // 数据管理
    VpnAccountLog: () => import('@/views/dataManager/vpnAccount-log'),
    AppStatistics: () => import('@/views/dataManager/appStatistics'),
    GoogleAdmobApi: () => import('@/views/dataManager/googleAdmobApi')
};

// 前端未找到页面路由（固定不用改）
const notFoundRouter = {
    path: '*', redirect: '/dashboard', hidden: true
};

/**
 * 格式化 后端 结构信息并递归生成层级路由表
 *
 * @param routerMap
 * @param parent
 * @returns {*}
 */
const generator = (routerMap, parent) => {
    return routerMap.map(item => {
        let target = '';
        if (item.path.indexOf('http://') === 0 || item.path.indexOf('https://') === 0) {
            target = '_blank';
        }

        const currentRouter = {
            path: item.path || '/',
            name: item.name || item.id || item.path || '',
            component: constantRouterComponents[item.component || 'BlankLayout'],
            meta: { title: item.title, icon: item.icon || undefined, target: target, keepAlive: item.keepAlive || false }
        };
        // 为了防止出现后端返回结果不规范，处理有可能出现拼接出两个 反斜杠
        currentRouter.path = currentRouter.path.replace('//', '/');

        item.hidden && (currentRouter.hidden = item.hidden);
        // 重定向
        item.redirect && (currentRouter.redirect = item.redirect);
        // 是否有子菜单，并递归处理
        if (item.children && item.children.length > 0) {
            // Recursion
            currentRouter.children = generator(item.children, currentRouter);
        }
        return currentRouter;
    });
};

export const generatorRouter = (routerMap, parent) => {
    const routers = generator(routerMap, parent);
    routers.push(notFoundRouter);
    return routers;
};
