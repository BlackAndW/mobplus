<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'app.id', {initialValue: model.app}]"
                    />
                </a-form-item>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="展示位代码">
                    <a-input
                        v-decorator="[ 'code', {initialValue: model.code, rules: [ { required: true, message: '请输入展示位代码' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="展示位名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入展示位名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="版本">
                    <a-checkbox :indeterminate="indeterminateVersion" :checked="checkAllVersion" @change="onVersionCheckAllChange">
                        全选
                    </a-checkbox>
                    <a-checkbox-group
                        @change="onVersionChange"
                        v-decorator="[ 'appVersionCheckList', {initialValue: model.appVersionCheckList, rules: [ { required: true, message: '请选择版本' }] }]">
                        <a-row class="checkbox-width">
                            <a-col
                                v-for="appVersion in appVersionList"
                                :key="appVersion.id"
                                span="6">
                                    <a-checkbox
                                        :value="appVersion.code"
                                        >{{ appVersion.code }}
                                    </a-checkbox>
                            </a-col>
                        </a-row>
                    </a-checkbox-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="渠道">
                    <a-checkbox :indeterminate="indeterminateChannel" :checked="checkAllChannel" @change="onChannelCheckAllChange">
                        全选
                    </a-checkbox>
                    <a-checkbox-group
                        @change="onChannelChange"
                        v-decorator="[ 'channelCheckList', {initialValue: model.channelCheckList, rules: [ { required: true, message: '请选择渠道' }] }]">
                        <a-row class="checkbox-width">
                            <a-col
                                v-for="channel in channelList"
                                :key="channel.id"
                                span="6">
                                    <a-checkbox
                                        :value="channel.code"
                                        >{{ channel.name }}
                                    </a-checkbox>
                            </a-col>
                        </a-row>
                    </a-checkbox-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="全局展示次数配置">
                    <a-radio-group @change="changeShowConfig" button-style="solid" v-decorator="[ 'limitShowConfig', {initialValue: model.limitShowConfig || 0}]">
                        <a-radio-button :value="1">开启</a-radio-button>
                        <a-radio-button :value="0">关闭</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="全局点击次数配置">
                    <a-radio-group @change="changeClickConfig" button-style="solid" v-decorator="[ 'limitClickConfig', {initialValue: model.limitClickConfig || 0}]">
                        <a-radio-button :value="1">开启</a-radio-button>
                        <a-radio-button :value="0">关闭</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否展示广告">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 1, rules: [ { required: true, message: '请选择是否展示广告' }]}]">
                        <a-radio-button :value="1">展示</a-radio-button>
                        <a-radio-button :value="2">隐藏</a-radio-button>
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
            appVersionList: [],
            channelList: [],
            indeterminateVersion: true,
            checkAllVersion: false,
            indeterminateChannel: true,
            checkAllChannel: false,
            advList: [],
            limitShowConfigFlag: 0,
            limitClickConfigFlag: 0,
            func: () => {}
        };
    },
    mounted () {
        this.loadAdvList();
    },
    computed: {
        AdType: function () {
            console.log(this.$DictFilterExclude(this.$AdType, [0]));
            return this.$DictFilterExclude(this.$AdType, [0]);
        }
    },
    methods: {
        add: function (currentApp) {
            this.title = '添加展示位';
            this.model = {};
            this.model.app = currentApp.key;
            this.url = '/app/position';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.limitShowConfigFlag = 0;
            this.limitClickConfigFlag = 0;
            this.visible = true;
            this.loadAppVersionList(currentApp);
            this.loadChannelList();
        },
        edit: function (record, currentApp) {
            this.title = '编辑展示位:' + record.id;
            this.model = record;
            this.model.app = currentApp.key;
            this.url = '/app/position/' + record.id;
            this.func = this.$http.put;
            this.confirmLoading = false;
            this.visible = true;
            this.limitShowConfigFlag = this.model.limitShowConfig;
            this.limitClickConfigFlag = this.model.limitClickConfig;
            this.loadAppVersionList(currentApp);
            this.loadChannelList();
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
            this.form.resetFields();
        },
        onCancel: function () {
            this.close(false);
        },
        changeShowConfig (e) {
            this.limitShowConfigFlag = e.target.value;
        },
        changeClickConfig (e) {
            this.limitClickConfigFlag = e.target.value;
        },
        onVersionChange () {
            this.$nextTick(() => {
                const checkedList = this.form.getFieldValue('appVersionCheckList');
                this.indeterminateVersion = !!checkedList.length && checkedList.length < this.appVersionList.length;
                this.checkAllVersion = checkedList.length === this.appVersionList.length;
            });
        },
        onVersionCheckAllChange (e) {
            const checkAllList = [];
            this.appVersionList.forEach(item => {
                checkAllList.push(item.code);
            });
            this.form.setFieldsValue({
                'appVersionCheckList': e.target.checked ? checkAllList : []
            });
            this.indeterminateVersion = false;
            this.checkAllVersion = e.target.checked;
        },
        onChannelChange () {
            this.$nextTick(() => {
                const checkedList = this.form.getFieldValue('channelCheckList');
                this.indeterminateChannel = !!checkedList.length && checkedList.length < this.channelList.length;
                this.checkAllChannel = checkedList.length === this.channelList.length;
            });
        },
        onChannelCheckAllChange (e) {
            const checkAllList = [];
            this.channelList.forEach(item => {
                checkAllList.push(item.code);
            });
            this.form.setFieldsValue({
                'channelCheckList': e.target.checked ? checkAllList : []
            });
            this.indeterminateChannel = false;
            this.checkAllChannel = e.target.checked;
        },
        loadChannelList: async function () {
            this.channelList = await this.$http.get('/release/channel/sct', {});
        },
        loadAppVersionList: async function (currentApp) {
            this.appVersionList = await this.$http.get('/app/version/sct', {
                appId: currentApp.key
            });
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
        loadAdvList: async function () {
            this.advList = await this.$http.get('/ad/adv/sct', {});
        }
    }
};
</script>

<style lang="less" scoped>
    .checkbox-width {
        width: 400px;
    }
</style>
