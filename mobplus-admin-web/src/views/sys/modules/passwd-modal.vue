<template>
    <a-modal
        :title="title"
        :width="modalWidth"
        :visible="visible"
        :confirmLoading="confirmLoading"
        @ok="onSubmit"
        @cancel="onCancel"
        cancelText="关闭"
    >
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="旧密码">
                    <a-input
                        type="password"
                        placeholder="请输入旧密码"
                        v-decorator="[ 'oldPasswd', validatorRules.oldPasswd]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="新密码">
                    <a-input
                        type="password"
                        placeholder="请输入新密码"
                        v-decorator="[ 'passwd', validatorRules.passwd]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="确认新密码">
                    <a-input
                        type="password"
                        @blur="onConfirmBlur"
                        placeholder="请确认新密码"
                        v-decorator="[ 'confirmPasswd', validatorRules.confirmPasswd]"
                    />
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
import { cryptoEncode } from '@/utils/util';

export default {
    data () {
        return {
            title: '修改密码',
            modalWidth: 700,
            visible: false,
            confirmLoading: false,
            validatorRules: {
                oldPasswd: {
                    rules: [
                        {
                            required: true,
                            message: '请输入旧密码!'
                        }
                    ]
                },
                passwd: {
                    rules: [
                        {
                            required: true,
                            message: '请输入新密码!'
                        },
                        {
                            validator: this.validateToNextPasswd
                        }
                    ]
                },
                confirmPasswd: {
                    rules: [
                        {
                            required: true,
                            message: '请确认新密码!'
                        },
                        {
                            validator: this.compareToFirstPasswd
                        }
                    ]
                }
            },
            confirmDirty: false,
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            form: this.$form.createForm(this)
        };
    },
    methods: {
        show: function (uname) {
            this.confirmLoading = false;
            this.visible = true;
        },
        onCancel: function () {
            this.close(false);
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },
        onSubmit: function () {
            const $self = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    const params = Object.assign({}, values);
                    params.oldPasswd = cryptoEncode(params.oldPasswd);
                    params.passwd = cryptoEncode(params.passwd);
                    params.confirmPasswd = cryptoEncode(params.confirmPasswd);
                    $self.confirmLoading = true;
                    $self.$http
                        .post('/auth/passwd', params)
                        .then(data => {
                            $self.$message.success(data || '修改密码成功');
                            $self.close(true);
                        })
                        .catch(err => {
                            if (err) {
                                console.log(err.stack);
                            }
                            $self.confirmLoading = false;
                        });
                }
            });
        },
        validateToNextPasswd (rule, value, callback) {
            const form = this.form;
            if (value && this.confirmDirty) {
                form.validateFields(['confirm'], { force: true });
            }
            callback();
        },
        compareToFirstPasswd (rule, value, callback) {
            const form = this.form;
            if (value && value !== form.getFieldValue('passwd')) {
                // eslint-disable-next-line standard/no-callback-literal
                callback('两次输入的密码不一样！');
            } else {
                callback();
            }
        },
        onConfirmBlur (e) {
            const value = e.target.value;
            this.confirmDirty = this.confirmDirty || !!value;
        }
    }
};
</script>

<style scoped>
</style>
