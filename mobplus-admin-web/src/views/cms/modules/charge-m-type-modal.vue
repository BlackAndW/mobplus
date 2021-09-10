<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="类型名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入分类名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="素材类别">
                    <a-radio-group button-style="solid" v-decorator="[ 'style', {initialValue: model.style || 1 , rules: [{ required: true }]}]">
                        <a-radio-button :value="1">视频</a-radio-button>
                        <a-radio-button :value="2">壁纸</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="类型优先级：">
                    <a-input
                        placeholder="默认最高为1"
                        v-decorator="[ 'rankOrder', { initialValue: model.rankOrder || 1 , rules: [{ required: true, message: '请输入优先级'}]}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="类型内排序依据">
                    <a-select
                        placeholder="选择排序依据"
                        @change="handleChangeOrder"
                        v-decorator="[ 'orderColumn', {initialValue: model.orderColumn, rules: [{ required: true, message: '请选择排序依据'}] }]"
                    >
                        <a-select-option
                            v-for="item in chargeOrderList"
                            :key="item.key"
                            :value="item.value"
                        >{{ item.key }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="升降序">
                    <a-radio-group button-style="solid" v-decorator="[ 'order', {initialValue: model.order || 1 }]">
                        <a-radio-button :value="1">降序</a-radio-button>
                        <a-radio-button :value="2">升序</a-radio-button>
                    </a-radio-group>
                </a-form-item>
            </a-form>
        </a-spin>
    </e-drawer>
</template>

<script>
import { mixinForm } from '@/utils/mixin';
import { EDrawer } from '@/components';
import { ChargeOrderList } from '@/datadict/datadict.js';

export default {
    mixins: [mixinForm],
    components: {
        EDrawer,
        ChargeOrderList
    },
    data () {
        return {
            title: '',
            visible: false,
            confirmLoading: false,
            form: this.$form.createForm(this),
            model: {},
            loading: false,
            chargeOrderList: ChargeOrderList,
            func: () => {}
        };
    },
    computed: { },
    methods: {
        add: function () {
            this.title = '新增分类';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.url = '/cms/charge/mtype';
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑分类:' + record.name;
            this.model = record;
            this.url = '/cms/charge/mtype/' + record.id;
            this.func = this.$http.put;
            this.confirmLoading = false;
            this.visible = true;
        },
        handleChangeOrder (params) {
            if (params === 'createdAt' || params === 'weight') {
                this.form.setFieldsValue({ order: 1 });
            } else {
                this.form.setFieldsValue({ order: 2 });
            }
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
                console.log(values);
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
