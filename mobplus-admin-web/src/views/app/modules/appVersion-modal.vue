<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="版本号">
                    <a-input
                        v-decorator="[ 'code', {initialValue: model.code, rules: [ { required: true, message: '请输入版本号' }]}]"
                        placeholder="版本号格式如：1.0"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="版本内容">
                    <a-textarea :rows="4" v-decorator="[ 'content', {initialValue: model.content}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 2, rules: [ { required: true, message: '请选择状态' }]}]">
                        <a-radio-button :value="1">未启用</a-radio-button>
                        <a-radio-button :value="2">已启用</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="更新配置">
                    <a-radio-group
                        button-style="solid"
                        @change="onChangeUpdateMethod"
                        v-decorator="[ 'updateMethod', {initialValue: model.updateMethod || 0, rules: [ { required: true, message: '更新配置' }]}]">
                        <a-radio-button :value="0">不启用</a-radio-button>
                        <a-radio-button :value="1">GP更新</a-radio-button>
                        <a-radio-button :value="2">应用内更新</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item v-if="updateMethod == 1" :labelCol="labelCol" :wrapperCol="wrapperCol" label="GP更新url">
                    <a-input
                        v-decorator="[ 'gpUrl', {initialValue: model.gpUrl, rules: [ { required: true, message: '请输入GP更新url' }] }]"
                    />
                </a-form-item>
                <a-form-item v-if="updateMethod == 2" :labelCol="labelCol" :wrapperCol="wrapperCol" label="应用内更新url">
                    <a-input
                        v-decorator="[ 'localUrl', {initialValue: model.localUrl, rules: [ { required: true, message: '请输入应用内更新url' }] }]"
                    />
                </a-form-item>
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'appId', {initialValue: model.appId}]"
                    />
                </a-form-item>
            </a-form>
        </a-spin>
    </e-drawer>
</template>

<script>
import { mixinForm } from '@/utils/mixin';
import { EDrawer } from '@/components';

export default {
    mixins: [mixinForm],
    components: {
        EDrawer
    },
    data () {
        return {
            appId: '',
            title: '',
            visible: false,
            confirmLoading: false,
            form: this.$form.createForm(this),
            model: {},
            updateMethod: 0,
            func: () => {}
        };
    },
    mounted () {
    },
    methods: {
        add: function (currentApp) {
            this.title = '添加版本：' + currentApp.title;
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.appId = currentApp.key;
            this.model = {
                appId: this.appId
            };
            this.url = '/app/version';
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑版本';
            this.model = record;
            this.url = '/app/version/' + record.id;
            this.func = this.$http.put;
            this.confirmLoading = false;
            this.updateMethod = record.updateMethod;
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
        onChangeUpdateMethod  (e) {
            this.updateMethod = e.target.value;
        },
        onSubmit: function () {
            const $self = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    $self.confirmLoading = true;
                    $self
                        .func($self.url, values)
                        .then(data => {
                            $self.$message.success(data || '操作成功!');
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
