<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="游戏名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入游戏名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="缩略图">
                    <a-upload
                        name="file"
                        class="image-uploader"
                        list-type="picture-card"
                        :show-upload-list="false"
                        action="/api/file/upload/game/thumb"
                        :before-upload="beforeUpload"
                        @change="handleChange"
                        v-decorator="[ 'thumb',{initialValue: model.thumbUrl, rules: [ { required: true, message: '请上传缩略图' }] }]"
                    >
                        <img
                            v-if="model.thumbUrl"
                            :src="model.thumbUrl"
                        />
                        <div v-else>
                            <a-icon :type="loading ? 'loading' : 'plus'"/>
                            <div class="ant-upload-text">
                                上传缩略图
                            </div>
                        </div>
                    </a-upload>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'thumbUrl', {initialValue: model.thumbUrl}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="游戏链接">
                    <a-input
                        v-decorator="[ 'playUrl', {initialValue: model.playUrl, rules: [ { required: true, message: '请输入游戏URL' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="推荐类型">
                    <a-radio-group button-style="solid" v-decorator="[ 'type', {initialValue: model.type || 1}]">
                        <a-radio-button
                            v-for="item in RecommendedType"
                            :key="item.value"
                            :value="item.value"
                        >{{ item.label }}</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="游戏描述">
                    <a-textarea :rows="4" v-decorator="[ 'desc', {initialValue: model.desc}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-textarea :rows="4" v-decorator="[ 'remark', {initialValue: model.remark}]" />
                </a-form-item>
                 <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="游戏状态">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 1}]">
                        <a-radio-button
                            v-for="item in GameStatus"
                            :key="item.value"
                            :value="item.value"
                        >{{ item.label }}</a-radio-button>
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
            loading: false,
            func: () => {}
        };
    },
    computed: {
        GameStatus: function () {
            return this.$DictFilterExclude(this.$GameStatus, [0]);
        },
        RecommendedType: function () {
            return this.$DictFilterExclude(this.$RecommendedType, [0]);
        }

    },
    methods: {
        add: function () {
            this.title = '添加游戏';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.url = '/cms/game';
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑游戏:' + record.name;
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
                this.model.thumbUrl = 'http://game.mc.yomobi.net:8001/thumb/' + info.file.response.result.realName;
                this.loading = false;
            }
        },
        beforeUpload (thumb) {
            const isPicture = thumb.type === 'image/jpg' || thumb.type === 'image/png' || thumb.type === 'image/gif' || thumb.type === 'image/jpeg' || thumb.type === 'image/bmp';
            if (!isPicture) {
                this.$message.error('只能上传图片格式');
            }
            const isLt2M = thumb.size / 1024 / 1024 < 2;
            if (!isLt2M) {
                this.$message.error('缩略图大小必须小于2MB');
            }
            return isPicture && isLt2M;
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

<style>
.image-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
<style scoped>
</style>
