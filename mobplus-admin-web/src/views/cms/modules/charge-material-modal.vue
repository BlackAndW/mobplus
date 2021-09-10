<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
            <!--
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="素材编号">
                    <a-input
                        placeholder="默认为id"
                        v-decorator="[ 'name', {initialValue: model.id || '', rules: [ { required: false, message: '请输入素材名' }] }]"
                    />
                </a-form-item>
            -->
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属分类">
                    <a-select
                        placeholder="选择类型"
                        v-decorator="[ 'type.id', { initialValue: 1 } ]"
                        :disabled="isTest"
                    >
                        <a-select-option
                            v-for="item in typeList"
                            :key="item.name"
                            :value="item.id"
                        >{{ item.name }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="封面图">
                    <a-upload
                        name="file"
                        class="image-uploader"
                        list-type="picture-card"
                        :show-upload-list="false"
                        action="/api/file/upload/aws/img/videoCover"
                        @change="handleChange('cover', $event)"
                    >
                        <img
                            class="cover-img"
                            v-if="model.videoCoverThumb"
                            :src="model.videoCoverThumb"
                        />
                        <div v-else>
                            <a-icon :type="loading ? 'loading' : 'plus'"/>
                            <div class="ant-upload-text">
                                上传封面
                            </div>
                        </div>
                    </a-upload>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'videoCover', {initialValue: model.videoCover, rules: [ { required: true, message: '请上传封面图' }] }]"
                    />
                    <a-input
                        type="hidden"
                        v-decorator="[ 'videoCoverThumb', {initialValue: model.videoCoverThumb }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="源视频：">
                    <a-upload
                        name="fileV"
                        action="/api/file/upload/aws/video/material"
                        :file-list="videoList"
                        :before-upload="beforeUpload"
                        @change="handleChange('v1', $event)"
                    >
                    <a-button> <a-icon type="upload" /> 上传源视频 </a-button>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'videoName', {initialValue: model.videoName, rules: [ { required: true, message: '请上传源视频' }] }]"
                    />
                    </a-upload>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="预览视频：">
                    <a-upload
                        name="fileV"
                        action="/api/file/upload/aws/video/material"
                        :file-list="videoIntroList"
                        :before-upload="beforeUpload"
                        @change="handleChange('v2', $event)"
                    >
                    <a-button> <a-icon type="upload" /> 上传预览视频 </a-button>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'videoIntroduceName', {initialValue: model.videoIntroduceName, rules: [ { required: true, message: '请上传预览视频' }] }]"
                    />
                    </a-upload>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="登录限制：">
                    <a-radio-group button-style="solid" v-decorator="[ 'useLimit', {initialValue: model.useLimit || 2 }]">
                        <a-radio-button :value="1">限制</a-radio-button>
                        <a-radio-button :value="2">无限制</a-radio-button>
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
            typeList: [],
            videoList: [],
            isTest: false,
            videoIntroList: [],
            loading: false,
            func: () => {}
        };
    },
    computed: {},
    mounted () {
    },
    methods: {
        add: function (queryParam, typeList) {
            this.title = '新增素材';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.videoList = [];
            this.videoIntroList = [];
            this.typeList = typeList;
            this.url = '/cms/charge/material';
            this.isTestMode(queryParam);
            this.visible = true;
        },
        edit: function (record, queryParam, typeList) {
            this.title = '编辑:' + record.id;
            this.model = record;
            this.videoList = [];
            this.videoIntroList = [];
            this.videoList.push({ uid: record.id, name: record.videoName });
            this.videoIntroList.push({ uid: record.id, name: record.videoIntroduceName });
            this.typeList = typeList;
            this.url = '/cms/charge/material/' + record.id;
            this.func = this.$http.put;
            this.isTestMode(queryParam);
            this.confirmLoading = false;
            this.visible = true;
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },
        // 同名文件免上传
        beforeUpload (file, fileList) {
            return new Promise((resolve, reject) => {
                this.$http.get('/cms/charge/material/isCopy?name=' + file.name).then(res => {
                    if (res === 1) {
                        // 已提交的同名文件
                        this.copyFile(file, fileList);
                        return false;
                    } else if (res === 2) {
                        // 已上传，未提交的同名文件
                        if (this.form.getFieldValue('videoName') === file.name ||
                            this.form.getFieldValue('videoIntroduceName') === file.name) {
                                this.copyFile(file, fileList);
                                return false;
                        }
                        return resolve(true);
                    }
                });
            });
        },
        // 判断是否为测试模式
        isTestMode (queryParam) {
            if (queryParam.type === 99) {
                this.isTest = true;
                this.$nextTick(() => {
                    this.form.setFieldsValue({ 'type.id': 99 });
                });
            } else {
                this.isTest = false;
            }
        },
        // file-list复制，form表单值复制
        copyFile (file, fileList) {
            this.videoList = this.videoIntroList = fileList;
            this.form.setFieldsValue({ videoName: file.name });
            this.form.setFieldsValue({ videoIntroduceName: file.name });
        },
        handleChange (params, info) {
            // 删除列表项时，对应表单值置为空
            if (info.file.status === 'removed') {
                if (params === 'v1') {
                    this.videoList = [];
                    this.form.setFieldsValue({ videoName: '' });
                }
                if (params === 'v2') {
                    this.videoIntroList = [];
                    this.form.setFieldsValue({ videoIntroduceName: '' });
                }
            } else if (info.file.status === 'uploading') {
                this.loading = true;
                if (params === 'v1') {
                    this.videoList = [...info.fileList];
                } else if (params === 'v2') {
                    this.videoIntroList = [...info.fileList];
                }
            // 上传完成后，对应表单项赋值
            } else if (info.file.status === 'done') {
                if (info.file.response.code === 5000) {
                    this.$message.error(info.file.response.message);
                    this.loading = false;
                } else if (info.file.response.code === 2000) {
                    if (params === 'cover') {
                        this.model.videoCover = info.file.response.result.url;
                        this.model.videoCoverThumb = info.file.response.result.thumbUrl;
                    }
                    if (params === 'v1') {
                        this.form.setFieldsValue({ videoName: info.file.response.result.realName });
                    }
                    if (params === 'v2') {
                        this.form.setFieldsValue({ videoIntroduceName: info.file.response.result.realName });
                    }
                    this.loading = false;
                }
            }
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

<style lang="less" scoped>
.cover-img {
    width: 80px;
}
.video-preview {
    width: 200px;
}
.video-view {
    width: 200px;
}
</style>
