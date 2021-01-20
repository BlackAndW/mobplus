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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="活动名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入活动名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="每日抽奖次数">
                    <a-input
                        v-decorator="[ 'drawCounts', {initialValue: model.drawCounts, rules: [ { required: true, message: '请输入抽奖次数' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="每次消耗的金币">
                    <a-input
                        v-decorator="[ 'needCoin', {initialValue: model.needCoin, rules: [ { required: true, message: '请输入每次消耗的金币' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单次抽取金币上限">
                    <a-input
                        v-decorator="[ 'maxBonusCoin', {initialValue: model.maxBonusCoin, rules: [ { required: true, message: '请输入单次抽取金币上限' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单次抽取金币下限">
                    <a-input
                        v-decorator="[ 'minBonusCoin', {initialValue: model.minBonusCoin, rules: [ { required: true, message: '请输入单次抽取金币下限' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="活动版本">
                    <a-input
                        v-decorator="[ 'version', {initialValue: model.version, rules: [ { required: true, message: '请输入活动版本' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否开启活动">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 1, rules: [ { required: true, message: '请选择是否开启活动' }]}]">
                        <a-radio-button :value="1">开启</a-radio-button>
                        <a-radio-button :value="2">关闭</a-radio-button>
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

            advList: [],
            func: () => {}
        };
    },
    mounted () {
    },
    computed: {
        // AdType: function () {
        //     return this.$DictFilterExclude(this.$AdType, [0]);
        // }
    },
    methods: {
        add: function (currentApp) {
            this.title = '添加活动';
            this.model = {};
            this.model.app = currentApp.key;
            this.url = '/app/activity';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record, currentApp) {
            this.title = '编辑活动:' + record.id;
            this.model = record;
            this.model.app = currentApp.key;
            this.url = '/app/activity/' + record.id;
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
