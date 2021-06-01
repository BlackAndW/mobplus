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
