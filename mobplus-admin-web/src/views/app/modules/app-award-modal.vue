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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="奖品类型">
                    <a-select
                        placeholder="==请选择奖品类型=="
                        v-decorator="['awardType', {initialValue: model.awardType, rules: [{ required: true, message: '请选择奖品类型'}]} ]"
                    >
                        <a-select-option
                            v-for="item in AwardTypeList"
                            :key="item.value"
                            :value="item.value"
                        >{{ item.label }}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="奖品名称">
                    <a-input
                        v-decorator="[ 'awardName', {initialValue: model.awardName, rules: [ { required: true, message: '请输入奖品名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="奖品图片">
                     <a-upload
                        name="file"
                        class="image-uploader"
                        list-type="picture-card"
                        :show-upload-list="false"
                        action="/api/file/upload/app/icon"
                        :before-upload="beforeUpload"
                        @change="handleChange"
                    >
                        <img
                            v-if="model.awardImgPath"
                            :src="model.awardImgPath"
                        />
                        <div v-else>
                            <a-icon :type="loading ? 'loading' : 'plus'"/>
                            <div class="ant-upload-text">
                                上传图标
                            </div>
                        </div>
                    </a-upload>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'awardImgPath', {initialValue: model.awardImgPath }]"
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
            AwardTypeList: [{ 'label': '一等奖', 'value': 1 }, { 'label': '二等奖', 'value': 2 }, { 'label': '三等奖', 'value': 3 }],
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
            this.model.activityId = currentAppActivity;
            this.url = '/app/activity/award/';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record, currentAppActivity) {
            this.title = '编辑奖品:' + record.id;
            this.model = record;
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
        handleChange (info) {
            if (info.file.status === 'uploading') {
                this.loading = true;
                return;
            }
            if (info.file.status === 'done') {
                this.model.awardImgPath = info.file.response.result.url;
                this.loading = false;
            }
        },
        beforeUpload (icon) {
            const isPicture = icon.type === 'image/jpg' || icon.type === 'image/png' || icon.type === 'image/gif' || icon.type === 'image/jpeg' || icon.type === 'image/bmp';
            if (!isPicture) {
                this.$message.error('只能上传图片格式');
            }
            const isLt2M = icon.size / 1024 / 1024 < 2;
            if (!isLt2M) {
                this.$message.error('缩略图大小必须小于2MB');
            }
            return isPicture && isLt2M;
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
