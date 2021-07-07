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
                        v-decorator="[ 'thumb-cover',{initialValue: model.videoCover, rules: [ { required: true, message: '请上传封面图' }] }]"
                    >
                        <img
                            class="cover-img"
                            v-if="model.videoCover"
                            :src="model.videoCover"
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
                        v-decorator="[ 'videoCover', {initialValue: model.videoCover}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="源视频：">
                    <a-upload
                        name="fileV"
                        action="/api/file/upload/aws/video/material"
                        :file-list="videoList"
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
                    <a-radio-group button-style="solid" v-decorator="[ 'useLimit', {initialValue: model.useLimit || 1 }]">
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
            videoIntroList: [],
            loading: false,
            func: () => {}
        };
    },
    computed: {},
    mounted () {
        this.getTypeList();
    },
    methods: {
        add: function () {
            this.title = '新增素材';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.videoList = [];
            this.videoIntroList = [];
            this.url = '/cms/charge/material';
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑:' + record.id;
            this.model = record;
            this.videoList = [];
            this.videoIntroList = [];
            this.videoList.push({ uid: 1, name: record.videoName });
            this.videoIntroList.push({ uid: 2, name: record.videoIntroduceName });
            this.url = '/cms/charge/material/' + record.id;
            this.func = this.$http.put;
            this.confirmLoading = false;
            this.visible = true;
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },
        getTypeList () {
            return this.$http.get('/cms/charge/mtype').then(res => {
                this.typeList = res.data;
            });
        },
        remove (params, info) {
            if (params === 'v1') {
                this.form.setFieldsValue({ videoName: '' });
            }
            if (params === 'v2') {
                this.form.setFieldsValue({ videoIntroduceName: '' });
            }
        },
        handleChange (params, info) {
            // 删除列表项时，对应表单值置为空
            if (info.file.status === 'removed') {
                console.log(info);
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
                    console.log(info);
                    if (params === 'cover') {
                        this.model.videoCover = info.file.response.result.url;
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
                console.log(values);
                if (!err) {
                    $self.confirmLoading = true;
                    $self
                        .func($self.url, values)
                        .then(data => {
                            $self.$message.success(data || '操作成功!');
                            this.form.resetFields();
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
