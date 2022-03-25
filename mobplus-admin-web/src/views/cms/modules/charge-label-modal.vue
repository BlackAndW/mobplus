<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="中文名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入中文名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="英文名称">
                    <a-input
                        v-decorator="[ 'enName', {initialValue: model.enName, rules: [ { required: true, message: '请输入英文名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标签类型">
                    <a-radio-group button-style="solid" v-decorator="[ 'type', {initialValue: model.type || 2, rules: [ { required: true, message: '请选择标签类型' }]}]">
                        <a-radio-button :value="1">热门</a-radio-button>
                        <a-radio-button :value="2">普通</a-radio-button>
                    </a-radio-group>
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
            isVip: 0,
            loading: false,
            func: () => {}
        };
    },
    computed: { },
    methods: {
        add: function () {
            this.title = '新增标签';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.url = '/cms/charge/label';
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑标签:' + record.id;
            this.model = record;
            this.url = '/cms/charge/label/' + record.id;
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
                // console.log(values);
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

<style lang="less" scoped>
</style>
