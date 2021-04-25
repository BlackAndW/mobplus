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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="广告类型">
                    <a-select
                        placeholder="==请选择广告类型=="
                        v-decorator="['type.id', {initialValue: model.type, rules: [{ required: true, message: '请选择广告类型'}]} ]"
                    >
                        <a-select-option
                            v-for="item in AdType"
                            :key="item.value"
                            :value="item.value"
                        >{{ item.label }}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="广告位代码">
                    <a-input
                        v-decorator="[ 'code', {initialValue: model.code, rules: [ { required: true, message: '请输入广告位代码' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="广告位名称">
                    <a-input
                        placeholder="==位置-广告类型=="
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入广告位名称' }] }]"
                    />
                </a-form-item>
                <a-form-item v-if="model.advName == 'mintegral'" :labelCol="labelCol" :wrapperCol="wrapperCol" label="mintegral单元id">
                    <a-input
                        v-decorator="[ 'mintegralUnitId', {initialValue: model.mintegralUnitId, rules: [ { required: true, message: '请输入mintegral单元id' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-textarea :rows="4" v-decorator="[ 'remark', {initialValue: model.remark}]" />
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
            this.title = '添加广告位：' + currentApp.title;
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.model.app = currentApp.key;
            this.url = '/ad/position';
            this.visible = true;
        },
        edit: function (record, currentApp) {
            this.title = '编辑广告位:' + currentApp.title;
            this.model = record;
            this.model.app = currentApp.key;
            this.url = '/ad/position/' + record.id;
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
