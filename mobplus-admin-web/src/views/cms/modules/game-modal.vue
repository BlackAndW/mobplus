<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <!-- <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'appId', {initialValue: model.appId}]"
                    />
                </a-form-item> -->
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入名称' }] }]"
                    />
                </a-form-item>
                <!-- <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="预览图">
                    <a-upload
                        name="file"
                        class="image-uploader"
                        list-type="picture-card"
                        :show-upload-list="false"
                        action="/api/file/upload/aws/img/game"
                        :before-upload="beforeUpload"
                        @change="handleChange"
                        v-decorator="[ 'thumb',{initialValue: model.imgPath, rules: [ { required: true, message: '请上传图片' }] }]"
                    >
                        <img
                            class="link-img"
                            v-if="model.imgPath"
                            :src="model.imgPath"
                        />
                        <div v-else>
                            <a-icon :type="loading ? 'loading' : 'plus'"/>
                            <div class="ant-upload-text">
                                上传
                            </div>
                        </div>
                    </a-upload>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'imgPath', {initialValue: model.imgPath}]"
                    />
                </a-form-item> -->
                <!-- <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="跳转链接：">
                    <a-input
                        v-decorator="[ 'imgUrl', {initialValue: model.imgUrl, rules: [ { required: true, message: '请输入跳转链接' }] }]"
                    />
                </a-form-item> -->
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
            loading: false,
            func: () => {}
        };
    },
    computed: { },
    methods: {
        add: function () {
            this.title = '新增';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.url = '/cms/game/';
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑:' + record.name;
            this.model = record;
            this.url = '/cms/game/' + record.id;
            this.func = this.$http.put;
            this.confirmLoading = false;
            this.visible = true;
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },
        handleChange (info) {
            if (info.file.status === 'uploading') {
                this.loading = true;
                return;
            }
            if (info.file.status === 'done') {
                console.log(info);
                this.model.imgPath = info.file.response.result.url;
                console.log(this.model.imgPath);
                this.loading = false;
            }
        },
        beforeUpload (thumb) {
            const isPicture = thumb.type === 'image/jpg' || thumb.type === 'image/png' || thumb.type === 'image/gif' || thumb.type === 'image/jpeg' || thumb.type === 'image/bmp';
            if (!isPicture) {
                this.$message.error('只能上传图片格式');
            }
            return isPicture;
        },
        onCancel: function () {
            this.close(false);
        },
        onSubmit: function () {
            const $self = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                console.log(values);
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
.link-img {
    width: 180px;
}
</style>
