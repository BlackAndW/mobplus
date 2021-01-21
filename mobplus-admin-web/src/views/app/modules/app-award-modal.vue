<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="奖品类别">
                    <a-input
                        v-decorator="[ 'awardType', {initialValue: model.awardType, rules: [ { required: true, message: '请输入奖品名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="奖品名称">
                    <a-input
                        v-decorator="[ 'awardName', {initialValue: model.awardName, rules: [ { required: true, message: '请输入奖品名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="奖品图片">
                    <a-input
                        v-decorator="[ 'awardImgPath', {initialValue: model.awardImgPath, rules: [ { required: true, message: '请输入奖品图片' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="奖品碎片">
                    <a-input
                        v-decorator="[ 'awardPiece', {initialValue: model.awardPiece, rules: [ { required: true, message: '请输入奖品碎片' }] }]"
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
            currentAppActivity: null,
            func: () => {}
        };
    },
    mounted () {
    },
    computed: {},
    methods: {
        add: function (currentAppActivity) {
            this.title = '添加奖品';
            this.model = {};
            this.model.appActivity = currentAppActivity.key;
            this.url = '/app/activity/award/';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record, currentAppActivity) {
            this.title = '编辑奖品:' + record.id;
            this.model = record;
            console.log(record);
            this.model.activityId = currentAppActivity;
            this.url = '/app/activity/award/' + record.id;
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
