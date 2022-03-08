<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'appId', {initialValue: model.appId}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="书籍名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入书籍名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="作者">
                    <a-input
                        v-decorator="[ 'author', {initialValue: model.author, rules: [ { required: true, message: '请输入作者名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="封面图">
                    <a-upload
                        name="file"
                        class="image-uploader"
                        list-type="picture-card"
                        :show-upload-list="false"
                        action="/api/file/upload/aws/img/bookCover"
                        :before-upload="beforeUpload"
                        @change="handleChange"
                        v-decorator="[ 'thumb',{initialValue: model.cover, rules: [ { required: true, message: '请上传封面图' }] }]"
                    >
                        <img
                            class="book-img"
                            v-if="model.cover"
                            :src="model.cover"
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
                        v-decorator="[ 'cover', {initialValue: model.cover}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="简介：">
                    <a-input
                        type="textarea"
                        v-decorator="[ 'description', {initialValue: model.description }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="书籍状态">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 1, rules: [ { required: true, message: '请选择书籍类型' }]}]">
                        <a-radio-button :value="0">连载中</a-radio-button>
                        <a-radio-button :value="2">更新中</a-radio-button>
                        <a-radio-button :value="1">完结</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="提供给">
                    <a-radio-group
                        button-style="solid"
                        @change="onChangeIsVip"
                        v-decorator="[ 'isVip', {initialValue: model.isVip || 0, rules: [ { required: true, message: '请选择用户类型' }]}]">
                        <a-radio-button :value="1">会员</a-radio-button>
                        <a-radio-button :value="0">所有</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item v-if="isVip === 0" :labelCol="labelCol" :wrapperCol="wrapperCol" label="解锁类型">
                    <a-radio-group button-style="solid" v-decorator="[ 'isFree', {initialValue: model.isFree || 0, rules: [ { required: true, message: '请选择解锁类型' }]}]">
                        <a-radio-button :value="1">付费</a-radio-button>
                        <a-radio-button :value="0">免费</a-radio-button>
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
        add: function (currentApp) {
            this.title = '新增书籍';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.model.appId = currentApp.key;
            this.url = '/cms/book/manager';
            this.visible = true;
        },
        edit: function (record, currentApp) {
            this.title = '编辑书籍:' + record.name;
            this.model = record;
            this.model.appId = currentApp.key;
            this.url = '/cms/book/manager/' + record.id;
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
                // console.log(info);
                this.model.cover = info.file.response.result.url;
                // console.log(this.model.cover);
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
        onChangeIsVip  (e) {
            this.isVip = e.target.value;
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
.book-img {
    width: 180px;
}
</style>
