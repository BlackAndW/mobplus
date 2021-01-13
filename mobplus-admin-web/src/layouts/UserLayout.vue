<template>
    <div id="userLayout" class="container">
        <swiper :options="swiperOption" ref="mySwiper">
            <swiper-slide v-for="img in swiperImages" :key="img">
                <img :src="img" />
            </swiper-slide>
        </swiper>

        <div class="main">
            <route-view />
        </div>
    </div>

    <!-- <div id="userLayout" :class="['user-layout-wrapper', device]">
    <div class="container">
      <div class="top">
        <div class="header">
          <a href="/">
            <img src="~@/assets/logo.svg" class="logo" alt="logo">
            <span class="title">Ant Design</span>
          </a>
        </div>
        <div class="desc">
          Ant Design 是西湖区最具影响力的 Web 设计规范
        </div>
      </div>

      <route-view></route-view>

      <div class="footer">
        <div class="links">
          <a href="_self">帮助</a>
          <a href="_self">隐私</a>
          <a href="_self">条款</a>
        </div>
        <div class="copyright">
          Copyright &copy; 2018 白鹭学园技术组出品
        </div>
      </div>
    </div>
    </div>-->
</template>

<script>
import 'swiper/dist/css/swiper.css';
import { swiper, swiperSlide } from 'vue-awesome-swiper';

import RouteView from './RouteView';
import { mixinDevice } from '@/utils/mixin';

const images = [
    'http://java.bizpower.com/admin/static/img/login-bg/bg-1.jpg',
    'http://java.bizpower.com/admin/static/img/login-bg/bg-2.jpg',
    'http://java.bizpower.com/admin/static/img/login-bg/bg-3.jpg',
    'http://java.bizpower.com/admin/static/img/login-bg/bg-4.jpg',
    'http://java.bizpower.com/admin/static/img/login-bg/bg-5.jpg'
];

export default {
    name: 'UserLayout',
    components: { RouteView, swiper, swiperSlide },
    mixins: [mixinDevice],
    data () {
        return {
            swiperOption: {
                loop: true, // 循环模式选项
                effect: 'fade',
                speed: 2000,
                fadeEffect: {
                    crossFade: true
                },
                autoplay: {
                    delay: 5000,
                    stopOnLastSlide: false,
                    disableOnInteraction: true
                }
            },

            swiperImages: images
        };
    },
    mounted () {
        document.body.classList.add('userLayout');
    },
    beforeDestroy () {
        document.body.classList.remove('userLayout');
    }
};
</script>

<style lang="less" scoped>
@--bg-color: rgba(0, 0, 0, 0.4);
@--font-color: #f2f6fc;
@--placeholder-color: #999;
@--padding: 40px;
@--radius: 4px;

.container {
    width: 100%;
    height: 100%;
    z-index: -2;
    background: url('../assets/login-bg.png') no-repeat;
    background-size: cover;

    .swiper-container {
        width: 100%;
        height: 100%;
    }

    .main {
        z-index: 999;
        position: absolute;
        display: table;
        box-sizing: border-box;
        height: auto;
        top: 50%;
        left: 50%;
        // min-width: 480px;
        // min-height: 320px;
        transform: translate(-50%, -50%);
        color: @--font-color;
        background: @--bg-color;
        padding: @--padding;
        border-radius: @--radius;
    }
    @media screen and (max-width: 1366px) {
        .main {
            transform: scale(0.9) translate(-50%, -50%);
        }
    }
    @media screen and (min-width: 2500px) {
        .main {
            transform: scale(1.24) translate(-50%, -50%);
        }
    }
    @media screen and (min-width: 4000px) {
        .main {
            transform: scale(2) translate(-25%, -50%);
        }
    }
}
</style>
 <style lang="less" scoped>
//   #userLayout.user-layout-wrapper {
//     height: 100%;

//     &.mobile {
//       .container {
//         .main {
//           max-width: 368px;
//           width: 98%;
//         }
//       }
//     }

//     .container {
//       width: 100%;
//       min-height: 100%;
//       background: #f0f2f5 url(~@/assets/background.svg) no-repeat 50%;
//       background-size: 100%;
//       padding: 110px 0 144px;
//       position: relative;

//       a {
//         text-decoration: none;
//       }

//       .top {
//         text-align: center;

//         .header {
//           height: 44px;
//           line-height: 44px;

//           .badge {
//             position: absolute;
//             display: inline-block;
//             line-height: 1;
//             vertical-align: middle;
//             margin-left: -12px;
//             margin-top: -10px;
//             opacity: 0.8;
//           }

//           .logo {
//             height: 44px;
//             vertical-align: top;
//             margin-right: 16px;
//             border-style: none;
//           }

//           .title {
//             font-size: 33px;
//             color: rgba(0, 0, 0, .85);
//             font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
//             font-weight: 600;
//             position: relative;
//             top: 2px;
//           }
//         }
//         .desc {
//           font-size: 14px;
//           color: rgba(0, 0, 0, 0.45);
//           margin-top: 12px;
//           margin-bottom: 40px;
//         }
//       }

//       .main {
//         min-width: 260px;
//         width: 368px;
//         margin: 0 auto;
//       }

//       .footer {
//         position: absolute;
//         width: 100%;
//         bottom: 0;
//         padding: 0 16px;
//         margin: 48px 0 24px;
//         text-align: center;

//         .links {
//           margin-bottom: 8px;
//           font-size: 14px;
//           a {
//             color: rgba(0, 0, 0, 0.45);
//             transition: all 0.3s;
//             &:not(:last-child) {
//               margin-right: 40px;
//             }
//           }
//         }
//         .copyright {
//           color: rgba(0, 0, 0, 0.45);
//           font-size: 14px;
//         }
//       }
//     }
//   }
</style>
