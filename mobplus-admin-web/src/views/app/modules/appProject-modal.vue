<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="版本">
                    <a-select placeholder="请选择版本" style="width:200px" v-decorator="[ 'appVersionId', {initialValue: model.appVersionId, rules: [ { required: true, message: '请选择版本' }]}]">
                        <a-select-option
                            v-for="appVersion in appVersionList"
                            :key="appVersion.id"
                            :value="appVersion.id"
                        >{{ appVersion.name }}     {{ appVersion.code }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="渠道">
                    <a-select placeholder="请选择渠道" style="width:200px" v-decorator="[ 'channelId', {initialValue: model.channelId, rules: [ { required: true, message: '请选择渠道' }]}]">
                        <a-select-option
                            v-for="channel in channelList"
                            :key="channel.id"
                            :value="channel.id"
                        >{{ channel.name }}     {{ channel.code }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-textarea :rows="4" v-decorator="[ 'remark', {initialValue: model.remark}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 2, rules: [ { required: true, message: '请选择项目状态' }]}]">
                        <a-radio-button :value="1">未启用</a-radio-button>
                        <a-radio-button :value="2">已启用</a-radio-button>
                    </a-radio-group>
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
            func: () => {},
            channelList: {},
            appVersionList: {}
        };
    },
    mounted () {
        this.loadChannelList();
    },
    methods: {

        loadChannelList: async function () {
            this.channelList = await this.$http.get('/release/channel/sct', {});
        },
        loadAppVersionList: async function (currentApp) {
            this.appVersionList = await this.$http.get('/app/version/sct', {
                appId: currentApp.key
            });
        },

        add: function (currentApp) {
            this.title = '添加项目：' + currentApp.title;
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.appId = currentApp.key;
            this.model = {
                appId: this.appId
            };
            this.url = '/app/project';
            this.visible = true;
            this.loadAppVersionList(currentApp);
        },
        edit: function (record, currentApp) {
            this.title = '编辑项目';
            this.model = record;
            this.url = '/app/project/' + record.id;
            this.func = this.$http.put;
            this.confirmLoading = false;
            this.visible = true;
            this.loadAppVersionList(currentApp);
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },
        onCancel: function () {
            this.close(false);
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
