<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                 <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="广告控制开关">
                    <a-radio-group button-style="solid" v-decorator="[ 'adSwitch', {initialValue: model.adSwitch || 1, rules: [ { required: true, message: '请选择开关' }]}]">
                        <a-radio-button :value="1">开启</a-radio-button>
                        <a-radio-button :value="2">关闭</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="内容控制开关">
                    <a-radio-group button-style="solid" v-decorator="[ 'contentSwitch', {initialValue: model.contentSwitch || 1, rules: [ { required: true, message: '请选择开关' }]}]">
                        <a-radio-button :value="1">开启</a-radio-button>
                        <a-radio-button :value="2">关闭</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="首页控制开关">
                    <a-radio-group button-style="solid" v-decorator="[ 'indexSwitch', {initialValue: model.indexSwitch || 1, rules: [ { required: true, message: '请选择开关' }]}]">
                        <a-radio-button :value="1">开启</a-radio-button>
                        <a-radio-button :value="2">关闭</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="配置">
                    <a-textarea
                        :row="8"
                        v-decorator="[ 'conf', {initialValue: model.conf}]"
                        placeholder="配置格式：JSON格式"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 2, rules: [ { required: true, message: '请选择状态' }]}]">
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
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'appProjectId', {initialValue: model.appProjectId}]"
                    />
                </a-form-item>
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'channelId', {initialValue: model.channelId}]"
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
            projectId: '',
            channelId: '',
            title: '',
            visible: false,
            confirmLoading: false,
            form: this.$form.createForm(this),
            model: {},
            func: () => {}
        };
    },
    mounted () {

    },
    methods: {
        edit: function (record, currentApp) {
            this.title = '编辑配置';
            this.model = record;
            this.url = '/app/config/' + record.id;
            this.func = this.$http.put;
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
