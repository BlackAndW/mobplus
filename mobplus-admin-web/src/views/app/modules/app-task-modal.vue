<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'appActivity.id', {initialValue: model.activityId}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="任务类型">
                    <a-select
                        placeholder="==请选择任务类型=="
                        v-decorator="['taskType', {initialValue: model.taskType, rules: [{ required: true, message: '请选择任务类型'}]} ]"
                    >
                        <a-select-option
                            v-for="item in TaskTypeList"
                            :key="item.value"
                            :value="item.value"
                        >{{ item.label }}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="任务名称">
                    <a-input
                        v-decorator="[ 'taskName', {initialValue: model.taskName, rules: [ { required: true, message: '请输入任务名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="任务奖励金币">
                    <a-input
                        v-decorator="[ 'taskBonusCoin', {initialValue: model.taskBonusCoin, rules: [ { required: true, message: '请输入任务奖励的金币' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="任务功能code">
                    <a-input
                        v-decorator="[ 'taskFunctionCode', {initialValue: model.taskFunctionCode, rules: [ { required: true, message: '请输入任务功能code' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="任务功能名">
                    <a-input
                        v-decorator="[ 'taskFunctionName', {initialValue: model.taskFunctionName, rules: [ { required: true, message: '请输入任务功能名' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否开启任务">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 1, rules: [ { required: true, message: '请选择是否开启任务' }]}]">
                        <a-radio-button :value="1">开启</a-radio-button>
                        <a-radio-button :value="2">关闭</a-radio-button>
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
            title: '',
            visible: false,
            confirmLoading: false,
            form: this.$form.createForm(this),
            model: {},
            currentAppActivity: null,
            TaskTypeList: [{ label: '新手任务', value: 1 }, { label: '日常任务', value: 2 }],
            func: () => {}
        };
    },
    mounted () {
    },
    computed: {},
    methods: {
        add: function (currentAppActivity) {
            this.title = '添加任务';
            this.model = {};
            this.model.activityId = currentAppActivity;
            this.url = '/app/activity/task/';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record, currentAppActivity) {
            this.title = '编辑任务:' + record.id;
            this.model = record;
            this.model.activityId = currentAppActivity;
            this.url = '/app/activity/task/' + record.id;
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
