<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'talk.id', {initialValue: model.talk}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="人物">
                    <a-radio-group button-style="solid" v-decorator="[ 'key', {initialValue: model.key || 1}]">
                        <a-radio-button :value="2">女</a-radio-button>
                        <a-radio-button :value="1">男</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="对话内容">
                    <a-input
                        v-decorator="[ 'value', {initialValue: model.value, rules: [ { required: true, message: '请输入对话内容' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="排序">
                    <a-input-number
                        :min="1"
                        v-decorator="[ 'sort', {initialValue: model.sort||1, rules: [ { required: true, message: '请输入排序' }] }]"
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
            title: '',
            visible: false,
            confirmLoading: false,
            form: this.$form.createForm(this),
            model: {},
            func: () => {}
        };
    },
    methods: {
        add: function (talkId) {
            this.title = '添加内容';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.model.talk = talkId;
            this.url = '/cms/talk/content';
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑内容';
            this.model = record;
            this.model.talk = record.talkId;
            this.url = '/cms/talk/content/' + record.id;
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
