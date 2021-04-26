<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'id', {initialValue: model.b01}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="节点名称">
                    <a-input
                        v-decorator="[ 'serverName', {initialValue: model.serverName, rules: [ { required: true, message: '请输入节点名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="国家">
                    <a-select placeholder="请选择国家" @change="handleCountryChange" style="width:200px" v-decorator="[ 'countryCode', {initialValue: model.countryCode, rules: [ { required: true, message: '请选择国家' }]}]">
                        <a-select-option
                            v-for="country in countryList"
                            :key="country.id"
                            :value="country.value"
                        >{{ country.name }}
                            <span>
                                <a-avatar style="height: 20px" shape="square" size="small" :src="'http://res.turbovpns.com/images/'+country.iconUrl" />
                            </span>
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <div v-show="false">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="节点英文名">
                        <a-input
                            v-decorator="[ 'b02', {initialValue: model.b02}]"
                        />
                    </a-form-item>
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="节点说明">
                        <a-input
                            v-decorator="[ 'b03', {initialValue: model.b03}]"
                        />
                    </a-form-item>
                </div>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="地区">
                    <a-input
                        v-decorator="[ 'region', {initialValue: model.region, rules: [ { required: true, message: '请输入地区名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="网速">
                    <a-input
                        v-decorator="[ 'b05', {initialValue: model.b05, rules: [ { required: true, message: '请输入网速' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="优先级">
                    <a-input
                        v-decorator="[ 'ratio', {initialValue: model.ratio, rules: [ { required: true, message: '请输入优先级（最高为1）' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="域名">
                    <a-input
                        :disabled="disabledIP"
                        v-decorator="[ 'ipAddr', {initialValue: model.ipAddr, rules: [ { required: true, message: '请输入节点域名' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否开启节点">
                    <a-radio-group button-style="solid" v-decorator="[ 'status', {initialValue: model.status || 2, rules: [ { required: true, message: '请选择是否开启节点' }]}]">
                        <a-radio-button :value="1">暂停</a-radio-button>
                        <a-radio-button :value="2">开启</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="选择节点类型">
                    <a-radio-group button-style="solid" v-decorator="[ 'type', {initialValue: model.type || 0, rules: [ { required: true, message: '请选择节点类型' }]}]">
                        <a-radio-button :value="0">普通节点</a-radio-button>
                        <a-radio-button :value="1">VIP节点</a-radio-button>
                        <a-radio-button :value="2">备用节点</a-radio-button>
                    </a-radio-group>
                </a-form-item>
            </a-form>
        </a-spin>
    </e-drawer>
</template>

<script>
import { mixinForm } from '@/utils/mixin';
import { EDrawer } from '@/components';
import { CountryList } from '@/datadict/datadict.js';

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
            countryList: CountryList,
            disabledIP: false,
            name_en: '',
            summary_en: '',
            func: () => {}
        };
    },
    mounted () {
    },
    computed: {},
    methods: {
        add: function () {
            this.title = '添加节点';
            this.model = {};
            this.url = '/app/vpn/';
            this.disabledIP = false;
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑节点:' + record.b01;
            this.model = record;
            this.url = '/app/vpn/' + record.b01;
            this.disabledIP = true;
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
        handleCountryChange (value) {
            for (const item of this.countryList) {
                if (item.value === value) {
                    this.form.setFieldsValue({
                        b02: item.name_en,
                        b03: item.summary_en
                    });
                }
            }
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

<style scoped>
</style>
