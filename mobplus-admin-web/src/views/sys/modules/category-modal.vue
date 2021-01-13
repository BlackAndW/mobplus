<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item
                    v-if="model.parentName"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="上级分类"
                >
                    <a-input
                        v-decorator="['parentName',{initialValue: model.parentName}]"
                        :readOnly="true"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="分类名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入分类名称' }] }]"
                    />
                </a-form-item>
                <!-- <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="英文名称">
                    <a-input v-decorator="[ 'nameEn', {initialValue: model.nameEn}]" />
                </a-form-item>-->

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="排序">
                    <a-input-number
                        :min="1"
                        :max="99"
                        v-decorator="[ 'sort', {initialValue: model.sort||1}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
                    <a-select v-decorator="[ 'status', {initialValue: model.status || 2}]">
                        <a-select-option
                            v-for="item in Status"
                            :key="item.value"
                            :value="item.value"
                        >{{ item.label }}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-textarea :rows="4" v-decorator="[ 'remark', {initialValue: model.remark}]" />
                </a-form-item>
            </a-form>
        </a-spin>
    </e-drawer>
</template>

<script>
import { EDrawer } from '@/components';

const url = '/sys/category';

export default {
    components: {
        EDrawer
    },
    data () {
        return {
            title: '',
            visible: false,
            confirmLoading: false,
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            form: this.$form.createForm(this),
            model: {},

            url: url,
            func: () => {}
        };
    },
    computed: {
        Status: function () {
            return this.$DictFilter(this.$GeneralStatus, [2, 3, 5]);
        }
    },
    methods: {
        add: function (record) {
            this.title = '添加数据分类';
            if (record && record !== undefined) {
                this.model = { parentId: record.id, parentName: record.name };
                this.url = url + '/' + record.id + '/item';
            } else {
                this.model = { parentId: 0, parentName: '一级分类' };
                this.url = url;
            }

            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑数据分类';
            this.model = record;
            this.url = url + '/' + record.id;
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
        }
    }
};
</script>

<style scoped>
</style>
