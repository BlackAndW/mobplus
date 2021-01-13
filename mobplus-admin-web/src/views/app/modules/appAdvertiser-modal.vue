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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="广告平台">
                    <a-select
                        placeholder="==请选择广告平台=="
                        v-decorator="['advertiser.id', {initialValue: model.advId, rules: [{ required: true, message: '请选择广告平台'}]} ]"
                    >
                        <a-select-option
                            v-for="adv in advList"
                            :key="adv.id"
                            :value="adv.id"
                        >{{ adv.name }}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="AppID">
                    <a-input
                        v-decorator="[ 'appId', {initialValue: model.appId}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="AppKey">
                    <a-input
                        v-decorator="[ 'appKey', {initialValue: model.appKey}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="应用名">
                    <a-input
                        v-decorator="[ 'appName', {initialValue: model.appName}]"
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
            advList: [],
            func: () => {}
        };
    },
    mounted () {
        this.loadAdvList();
    },
    computed: {
        AdType: function () {
            return this.$DictFilterExclude(this.$AdType, [0]);
        }
    },
    methods: {
        add: function (currentApp) {
            this.title = '添加广告平台配置：' + currentApp.title;
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.model.app = currentApp.key;
            this.url = '/app/advertiser';
            this.visible = true;
        },
        edit: function (record, currentApp) {
            this.title = '编辑广告平台配置';
            this.model = record;
            this.model.app = currentApp.key;
            this.url = '/app/advertiser/' + record.id;
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
        loadAdvList: async function () {
            this.advList = await this.$http.get('/ad/adv/sct', {});
        }
    }
};
</script>

<style scoped>
</style>
