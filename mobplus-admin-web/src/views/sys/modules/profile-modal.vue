<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="用户账号">
                    <a-input
                        v-decorator="[ 'userName', {initialValue: model.userName, rules: [ { required: true, message: '请输入用户账号' }] }]"
                        :readOnly="true"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="用户昵称">
                    <a-input
                        v-decorator="[ 'displayName', {initialValue: model.displayName, rules: [ { required: true, message: '请输入用户昵称' }] }]"
                    />
                </a-form-item>

                <!-- <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="部门">
                    <a-input
                        v-decorator="[ 'dptName', {initialValue: model.dptName}]"
                        :readOnly="true"
                    />
                </a-form-item>-->

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="姓名">
                    <a-input v-decorator="[ 'name', {initialValue: model.name}]" />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="头像">
                    <a-avatar
                        shape="square"
                        :src="model.avatar"
                        @click="onSelectAvatar"
                        style="cursor:pointer"
                    />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="性别">
                    <a-select
                        v-decorator="['gender', {initialValue: model.gender}]"
                        placeholder="请选择性别"
                    >
                        <a-select-option :value="1">男</a-select-option>
                        <a-select-option :value="0">女</a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="电话号码">
                    <a-input v-decorator="[ 'mobile', {initialValue: model.mobile}]" />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="邮箱">
                    <a-input v-decorator="[ 'email', {initialValue: model.email}]" />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="生日">
                    <a-date-picker
                        placeholder="请选择出生日期"
                        v-decorator="['birthday', {initialValue: model.birthday}]"
                    />
                </a-form-item>
            </a-form>
        </a-spin>
        <avatar-selector-modal ref="avatarModal" @change="onAvatarChange" />
    </e-drawer>
</template>

<script>
import moment from 'moment';
import { EDrawer, AvatarSelectorModal } from '@/components';

const url = '/auth/profile';

export default {
    components: {
        EDrawer,
        AvatarSelectorModal
    },
    data () {
        return {
            title: '个人信息',
            visible: false,
            confirmLoading: false,
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            dateFormat: 'YYYY-MM-DD',
            form: this.$form.createForm(this),
            model: {}
        };
    },
    methods: {
        show: function () {
            this.loadUserInfo();
            this.confirmLoading = false;
            this.visible = true;
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },
        onCancel: function () {
            this.close(false);
        },
        onSelectAvatar: function () {
            this.$refs.avatarModal.show(this.model.avatar);
        },
        onAvatarChange: function (avatar) {
            this.model.avatar = avatar;
        },
        // /*========================================================*/
        loadUserInfo: async function () {
            this.confirmLoading = true;
            try {
                const data = await this.$http.get(url);
                if (data) {
                    data.birthday =
                        data.birthday && moment(data.birthday, this.dateFormat);
                    this.model = data;
                }
            } catch (err) {
                this.close(false);
            } finally {
                this.confirmLoading = false;
            }
        },
        onSubmit: function () {
            const $self = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    values.avatar = $self.model.avatar;
                    $self.confirmLoading = true;
                    $self.$http
                        .put(url, values)
                        .then(data => {
                            $self.$message.success(data || '个人信息修改成功!');
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
        }
    }
};
</script>

<style scoped>
</style>
