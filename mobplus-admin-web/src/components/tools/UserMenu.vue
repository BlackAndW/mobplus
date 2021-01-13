<template>
    <div class="user-wrapper">
        <div class="content-box">
            <full-screen class="action" />
            <!-- <notice-icon class="action" />
            <lang-select />-->
            <a-dropdown>
                <span class="action ant-dropdown-link user-dropdown-menu">
                    <a-avatar class="avatar" :src="avatar" />
                    <span>{{ nickname }}</span>
                </span>
                <a-menu slot="overlay" class="user-dropdown-menu-wrapper">
                    <a-menu-item key="0">
                        <a href="javascript:;" @click="onUserInfo">
                            <a-icon type="user" />
                            <span>个人信息</span>
                        </a>
                    </a-menu-item>
                    <a-menu-item key="2">
                        <a href="javascript:;" @click="onUpdatePasswd">
                            <a-icon type="setting" />
                            <span>修改密码</span>
                        </a>
                    </a-menu-item>
                     <a-menu-item key="3">
                        <a href="javascript:;" @click="$refs.settingDrawer.showDrawer()">
                            <a-icon type="tool" />
                            <span>系统设置</span>
                        </a>
                    </a-menu-item>
                    <a-menu-divider />
                    <a-menu-item key="4">
                        <a href="javascript:;" @click="onLogout">
                            <a-icon type="logout" />
                            <span>退出登录</span>
                        </a>
                    </a-menu-item>
                </a-menu>
            </a-dropdown>
            <a-button shape="circle" icon="logout" @click="onLogout" />
        </div>
        <sys-passwd-modal ref="userPassword" />
        <sys-profile-modal ref="userProfile" />
        <!-- Setting Drawer (show in development mode) -->
        <setting-drawer ref="settingDrawer"></setting-drawer>
    </div>
</template>

<script>
// import NoticeIcon from '@/components/NoticeIcon'
// import LangSelect from '@/components/tools/LangSelect'
import FullScreen from '@/components/FullScreen';

import { mapActions, mapGetters } from 'vuex';
import SettingDrawer from '@/components/SettingDrawer';
import SysPasswdModal from '@/views/sys/modules/passwd-modal';
import SysProfileModal from '@/views/sys/modules/profile-modal';

export default {
    name: 'UserMenu',
    components: {
        // LangSelect,
        // NoticeIcon,
        FullScreen,
        SettingDrawer,
        SysPasswdModal,
        SysProfileModal
    },
    computed: {
        ...mapGetters(['nickname', 'avatar'])
    },
    methods: {
        ...mapActions(['Logout']),
        onUserInfo: function () {
            this.$refs.userProfile.show();
        },
        onUpdatePasswd: function () {
            this.$refs.userPassword.show();
        },
        onLogout: function () {
            this.$confirm({
                title: '提示',
                content: '真的要注销登录吗 ?',
                onOk: () => {
                    return this.Logout({})
                        .then(() => {
                            setTimeout(() => {
                                window.location.reload();
                            }, 16);
                        })
                        .catch(err => {
                            this.$message.error({
                                title: '错误',
                                description: err.message
                            });
                        });
                },
                onCancel () {}
            });
        }
    }
};
</script>
