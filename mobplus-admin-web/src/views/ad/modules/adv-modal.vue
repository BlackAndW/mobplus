<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="平台代码">
                    <a-input
                        v-decorator="[ 'code', {initialValue: model.code, rules: [ { required: true, message: '请输入分类平台代码' }] }]"
                        :readOnly="model.id!=null"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开屏">
                    <a-input
                        v-decorator="[ 'testId1', {initialValue: model.testId1 }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="横屏">
                    <a-input
                        v-decorator="[ 'testId2', {initialValue: model.testId2 }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="插屏">
                    <a-input
                        v-decorator="[ 'testId3', {initialValue: model.testId2 }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="插屏视频">
                    <a-input
                        v-decorator="[ 'testId4', {initialValue: model.testId4 }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="原生模板">
                    <a-input
                        v-decorator="[ 'testId5', {initialValue: model.testId5 }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="激励视频">
                    <a-input
                        v-decorator="[ 'testId6', {initialValue: model.testId6 }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="信息流">
                    <a-input
                        v-decorator="[ 'testId7', {initialValue: model.testId7 }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="横版信息流">
                    <a-input
                        v-decorator="[ 'testId8', {initialValue: model.testId8 }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-textarea :rows="4" v-decorator="[ 'remark', {initialValue: model.remark}]" />
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
            title: '',
            visible: false,
            confirmLoading: false,
            form: this.$form.createForm(this),
            model: {},
            func: () => {}
        };
    },
    methods: {
        add: function (record) {
            this.title = '添加广告平台';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.url = '/ad/adv';
            this.model = {};
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑广告平台:' + record.code;
            this.model = record;
            this.url = '/ad/adv/' + record.id;
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
