// import Vue from 'vue'
import { deviceEnquire, DEVICE_TYPE } from '@/utils/device';
import { mapState } from 'vuex';

// const mixinsComputed = Vue.config.optionMergeStrategies.computed
// const mixinsMethods = Vue.config.optionMergeStrategies.methods

const mixin = {
  computed: {
    ...mapState({
      layoutMode: state => state.app.layout,
      navTheme: state => state.app.theme,
      primaryColor: state => state.app.color,
      colorWeak: state => state.app.weak,
      fixedHeader: state => state.app.fixedHeader,
      fixSiderbar: state => state.app.fixSiderbar,
      fixSidebar: state => state.app.fixSiderbar,
      contentWidth: state => state.app.contentWidth,
      autoHideHeader: state => state.app.autoHideHeader,
      sidebarOpened: state => state.app.sidebar,
      multiTab: state => state.app.multiTab
    })
  },
  methods: {
    isTopMenu () {
      return this.layoutMode === 'topmenu';
    },
    isSideMenu () {
      return !this.isTopMenu();
    }
  }
};

const mixinDevice = {
  computed: {
    ...mapState({
      device: state => state.app.device
    })
  },
  methods: {
    isMobile () {
      return this.device === DEVICE_TYPE.MOBILE;
    },
    isDesktop () {
      return this.device === DEVICE_TYPE.DESKTOP;
    },
    isTablet () {
      return this.device === DEVICE_TYPE.TABLET;
    }
  }
};

const AppDeviceEnquire = {
  mounted () {
    const { $store } = this;
    deviceEnquire(deviceType => {
      switch (deviceType) {
        case DEVICE_TYPE.DESKTOP:
          $store.commit('TOGGLE_DEVICE', 'desktop');
          $store.dispatch('setSidebar', true);
          break;
        case DEVICE_TYPE.TABLET:
          $store.commit('TOGGLE_DEVICE', 'tablet');
          $store.dispatch('setSidebar', false);
          break;
        case DEVICE_TYPE.MOBILE:
        default:
          $store.commit('TOGGLE_DEVICE', 'mobile');
          $store.dispatch('setSidebar', true);
          break;
      }
    });
  }
};

const mixinForm = {
    data () {
        return {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            labelCol2: {
                xs: { span: 24 },
                sm: { span: 8 }
            },
            wrapperCol2: {
                xs: { span: 24 },
                sm: { span: 16 }
            }
        };
    },
    methods: {
        urlsToFileList: function (urls) {
            if (urls && urls.length > 0) {
                let idx = 0;
                return urls.map(url => {
                    idx++;
                    return { uid: '-' + idx, status: 'done', url: url, name: url };
                });
            }
            return [];
        },
        fileListToUrls: function (fileList) {
            const urls = [];
            fileList.forEach(file => {
                if (file.response && file.response.code === 2000 && file.response.result) {
                    urls.push(file.response.result.url);
                } else if (file.url) {
                    urls.push(file.url);
                }
            });
            return urls;
        }
    }
};
export { mixin, AppDeviceEnquire, mixinDevice, mixinForm };
