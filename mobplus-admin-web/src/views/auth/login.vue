<template>
    <a-form :form="form" id="loginForm" ref="loginForm" @submit="onSubmit">
        <div class="login-left">
            <img src="@/assets/logo-big.png" alt="YeeIMS" />
        </div>
        <div class="login-body">
            <a-form-item class="form-item">
                <a-input
                    type="text"
                    placeholder="请输入用户账号"
                    v-decorator="['userName',
                        {rules: [{ required: true, message: '请输入用户账号' }], validateTrigger: 'change'}
                    ]"
                ></a-input>
            </a-form-item>
            <a-form-item class="form-item">
                <a-input
                    type="password"
                    autocomplete="false"
                    placeholder="请输入密码"
                    v-decorator="[
                        'passwd',
                        {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}
                    ]"
                ></a-input>
            </a-form-item>
            <!-- <a-row :gutter="0">
                <a-col :span="14"> -->
                    <!-- 此为生产环境，暂时也去掉验证码-->
                    <a-form-item class="form-item captcha">
                        <a-input
                            autocomplete="false"
                            placeholder="请输入验证码"
                            v-decorator="[
                                'captcha',
                                {rules: [{ required: false, message: '请输入验证码' }], validateTrigger: 'blur'}
                            ]"
                        >
                        <img slot="addonAfter" ref="captcha" class="form-image" :src="captchaUrl" @click="refreshCaptcha()" />
                        </a-input>
                    </a-form-item>
                    <!-- 开发环境取消验证码
                    <a-form-item class="form-item captcha">
                        <a-input
                            autocomplete="false"
                            placeholder="开发环境就别输入验证码了"
                            v-decorator="[
                                'captcha',
                                {rules: [{ required: false, message: '请输入验证码' }], validateTrigger: 'blur'}
                            ]"
                        >
                        <img slot="addonAfter" ref="captcha" class="form-image" :src="captchaUrl" @click="refreshCaptcha()" />
                        </a-input>
                    </a-form-item>
                    -->
                <!-- </a-col>
                <a-col :span="10">
                    <img @click="refreshCaptcha()" class="form-image" :src="captchaUrl" style="float:right" />
                </a-col> -->
            </a-row>
            <a-form-item class="form-button">
                <a-button type="primary" @click.native.prevent="onSubmit" :loading="logining">登录</a-button>
            </a-form-item>
        </div>
    </a-form>
</template>

<script>
import { cryptoEncode } from '@/utils/util';
import { mapActions } from 'vuex';

export default {
    components: {},
    data () {
        return {
            logining: false,
            form: this.$form.createForm(this),
            captchaUrl: ''
        };
    },
    created () {},
    mounted () {
        this.$nextTick(() => {
            this.refreshCaptcha();
//            this.loadFormData();
        });
    },
    methods: {
        ...mapActions(['Login', 'Logout']),
        refreshCaptcha: function () {
            this.captchaUrl = process.env.VUE_APP_API_BASE_URI + '/auth/captcha.png?random' + new Date().getTime();
        },
        onSubmit: function () {
            const $self = this;
            this.logining = true;
            this.form.validateFields((err, values) => {
                if (!err) {
                    const params = Object.assign({}, values);
                    params.passwd = cryptoEncode(params.passwd);
                    $self
                        .Login(params)
                        .then(res => {
                            $self.$router.push('/');
                        })
                        .catch(err => {
                            $self.refreshCaptcha();
                            if (err) {
                                console.log(err.stack);
                            }
                            $self.logining = false;
                        });
                } else {
                    $self.logining = false;
                }
            });
        }
    }
};
</script>

<style lang="less">
@--bg-color: rgba(0, 0, 0, 0.4);
@--font-color: #f2f6fc;
@--placeholder-color: #999;
@--form-item-height: 40px;

.placeholder(@color) {
    ::-webkit-input-placeholder {
        /* WebKit browsers */
        color: @color;
    }
    :-moz-placeholder {
        /* Mozilla Firefox 4 to 18 */
        color: @color;
    }
    ::-moz-placeholder {
        /* Mozilla Firefox 19+ */
        color: @color;
    }
    :-ms-input-placeholder {
        /* Internet Explorer 10+ */
        color: @color;
    }
}
.login-left {
    display: table-cell;
    vertical-align: middle;
    padding-right: 30px;
    img {
        width: 250px;
    }
}
.login-body {
    vertical-align: top;
    display: table-cell;
    width: 320px;
    .form-item {
        //    box-shadow: none;
        //    background-color: @--bg-color;

        input {
            .placeholder(@--placeholder-color);
            border: none;
            color: @--font-color;
            background-color: @--bg-color;
            height: @--form-item-height;
            line-height: 1.33;
            padding: 10px 30px 10px 16px;
            font-size: 14px;
        }

        &.captcha{
            .form-image {
                width: 100px;
                height: @--form-item-height;
                background-color: transparent;
                border: none;
                cursor: pointer;
            }
            .ant-input-group-addon{
                padding: 0px;
                border: 0px;
            }
        }
    }

    .form-button {
        margin: 0px;
        padding-top: 18px;
        button {
            display: block;
            width: 100%;
            height: @--form-item-height;
            line-height: 1.33;
            padding: 10px 16px;
            border: none;
            color: #fff;
            box-shadow: none;
            font-size: 18px;
        }
    }
}
</style>
