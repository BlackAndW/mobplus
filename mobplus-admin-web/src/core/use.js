import Vue from 'vue';
import VueStorage from 'vue-ls';
import config from '@/config/defaultSettings';

// base library
import Antd from 'ant-design-vue';
import Viser from 'viser-vue';
import VueCropper from 'vue-cropper';
import PermissionHelper from '@/utils/helper/permission';
import 'ant-design-vue/dist/antd.less';
import '@/assets/index.less';

// ext library
// import '@/components/use'
import './directives/action';
import './directives/roles';
import '@/datadict';
import '@/filters';

Vue.use(Antd);
Vue.use(Viser);

Vue.use(VueStorage, config.storageOptions);
Vue.use(VueCropper);
Vue.use(PermissionHelper);
