<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="应用名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入应用名称' }]}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="应用包名">
                    <a-input
                        v-decorator="[ 'pkgName', {initialValue: model.pkgName, rules: [ { required: true, message: '请输入应用包名' }]}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="运行环境">
                    <a-select placeholder="运行环境" style="width:120px" v-decorator="[ 'runtime', {initialValue: model.runtime, rules: [ { required: true, message: '请选择运行环境' }]}]">
                        <a-select-option :value="1">android</a-select-option>
                        <a-select-option :value="2">ios</a-select-option>
                        <a-select-option :value="3">h5</a-select-option>
                        <a-select-option :value="4">wechat</a-select-option>
                        <a-select-option :value="5">qq</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="应用类型">
                    <a-radio-group button-style="solid" v-decorator="[ 'type', {initialValue: model.type || 1, rules: [ { required: true, message: '请选择应用类型' }]}]">
                        <a-radio-button :value="1">应用</a-radio-button>
                        <a-radio-button :value="2">游戏</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="图标">
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
                            v-if="model.iconPath"
                            :src="model.iconPath"
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
                        v-decorator="[ 'iconPath', {initialValue: model.iconPath }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="友盟—AppKey">
                    <a-input
                        v-decorator="[ 'extra.umengKey', {initialValue: getUmentKey(model) }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-textarea :rows="4" v-decorator="[ 'remark', {initialValue: model.remark}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 2, rules: [ { required: true, message: '请选择状态' }]}]">
                        <a-radio-button :value="1">未启用</a-radio-button>
                        <a-radio-button :value="2">已启用</a-radio-button>
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
            loading: false,
            model: {},
            func: () => {}
        };
    },
    methods: {
        getUmentKey: function (model) {
            if (model.extra && model.extra.umengKey) {
                return model.extra.umengKey;
            }
            return '';
        },
        add: function () {
            this.title = '添加应用';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.url = '/app/entity';
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑应用:' + record.name;
            this.model = record;
            this.url = '/app/entity/' + record.id;
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
        },
        handleChange (info) {
            if (info.file.status === 'uploading') {
                this.loading = true;
                return;
            }
            if (info.file.status === 'done') {
                this.model.iconPath = info.file.response.result.url;
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
        }
    }
};
</script>

<style scoped>
</style>
