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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属分类">
                    <a-select
                        placeholder="选择类型"
                        v-decorator="[ 'type.id', { initialValue: model.typeId, rules: [ { required: true, message: '请选择类型' }] } ]"
                    >
                        <a-select-option
                            v-for="item in typeList"
                            :key="item.name"
                            :value="item.id"
                        >{{ item.name }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="预览图">
                    <a-upload
                        name="file"
                        class="image-uploader"
                        list-type="picture-card"
                        :show-upload-list="false"
                        action="/api/file/upload/aws/img/wallpaper"
                        :before-upload="beforeUpload"
                        @change="handleChange"
                        v-decorator="[ 'thumb',{initialValue: model.imgUrl, rules: [ { required: true, message: '请上传壁纸图' }] }]"
                    >
                        <img
                            class="img-model"
                            v-if="model.imgUrl"
                            :src="model.imgUrl"
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
                        v-decorator="[ 'imgUrl', {initialValue: model.imgUrl}]"
                    />
                    <a-input
                        type="hidden"
                        v-decorator="[ 'thumbUrl', {initialValue: model.thumbUrl}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="登录限制：">
                    <a-radio-group button-style="solid" v-decorator="[ 'useLimit', {initialValue: model.useLimit || 2 }]">
                        <a-radio-button :value="1">限制</a-radio-button>
                        <a-radio-button :value="2">无限制</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标签：">
                    <a-select
                        mode="tags"
                        style="width: 200px"
                        v-decorator="['labels', {initialValue: model.labels} ]"
                        >
                        <a-select-option
                            v-for="label in labelList"
                            :key="label.id"
                            :value="label.name + '-' + label.enName">
                            {{ label.name + '-' + label.enName }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="上传到应用" v-if="checkItem">
                    <a-checkbox :indeterminate="indeterminateApp" :checked="checkAllApp" @change="onCheckAllChange">
                        全选
                    </a-checkbox><br />
                    <a-checkbox-group
                        @change="onAppChange"
                        v-decorator="[ 'appCheckList', {initialValue: appCheckList, rules: [ { required: true, message: '请选择应用' }] }]">
                        <a-row class="checkbox-width">
                            <a-col
                                v-for="app in appList"
                                :key="app.id"
                                span="24">
                                    <a-checkbox
                                        v-if="app.id === 43"
                                        :value="app.id"
                                        >(通用){{ app.name }}
                                    </a-checkbox>
                                    <a-checkbox
                                        v-else
                                        :value="app.id"
                                        >{{ app.name }}
                                    </a-checkbox>
                            </a-col>
                        </a-row>
                    </a-checkbox-group>
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
            typeList: [],
            confirmLoading: false,
            form: this.$form.createForm(this),
            model: {},
            loading: false,
            appList: [],
            appCheckList: [],
            indeterminateApp: true,
            checkAllApp: false,
            checkItem: true,
            labelList: [],
            func: () => {}
        };
    },
    computed: { },
    mounted () {
        this.loadAppList();
        this.loadLabelList();
    },
    methods: {
        add: function (typeList, currentApp) {
            this.title = '新增壁纸';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.model.appId = currentApp.key;
            this.typeList = typeList;
            this.url = '/cms/charge/material';
            this.checkItem = true;
            this.visible = true;
        },
        edit: function (record, typeList, currentApp) {
            this.title = '编辑壁纸:' + record.id;
            this.model = record;
            this.model.appId = currentApp.key;
            this.typeList = typeList;
            this.url = '/cms/charge/material/' + record.id;
            this.func = this.$http.put;
            this.checkItem = false;
            this.confirmLoading = false;
            this.visible = true;
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },

        loadAppList () {
            this.$http.get('/cms/charge/app/list')
            .then(res => {
                this.appList = res;
            });
        },
        loadLabelList () {
            this.$http.get('/cms/charge/label?pageNo=0&pageSize=999')
            .then(res => {
                this.labelList = res.data;
            });
        },
        onAppChange () {
            this.$nextTick(() => {
                const checkedList = this.form.getFieldValue('appCheckList');
                this.indeterminateApp = !!checkedList.length && checkedList.length < this.appList.length;
                this.checkAllApp = checkedList.length === this.appList.length;
            });
        },
        onCheckAllChange (e) {
            const checkAllList = [];
            this.appList.forEach(item => {
                checkAllList.push(item.id);
            });
            this.form.setFieldsValue({
                'appCheckList': e.target.checked ? checkAllList : []
            });
            this.indeterminateApp = false;
            this.checkAllApp = e.target.checked;
        },
        handleChange (info) {
            if (info.file.status === 'uploading') {
                this.loading = true;
                return;
            }
            if (info.file.status === 'done') {
                this.model.imgUrl = info.file.response.result.url;
                this.model.thumbUrl = info.file.response.result.thumbUrl;
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
.img-model {
    width: 180px;
}
</style>
