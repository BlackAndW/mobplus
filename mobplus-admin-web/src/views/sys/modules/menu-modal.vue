<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item
                    v-if="model.parentName!=null"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="上级菜单"
                >
                    <a-input
                        v-decorator="['parentName',{initialValue: model.parentName}]"
                        :readOnly="true"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="菜单标题">
                    <a-input
                        v-decorator="[ 'title', {initialValue: model.title, rules: [ { required: true, message: '请输入菜单标题' }] }]"
                    />
                </a-form-item>
                <!-- <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="英文标题">
                    <a-input v-decorator="[ 'titleEn', {initialValue: model.titleEn}]" />
                </a-form-item>-->
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="路由名称">
                    <a-input v-decorator="[ 'name', {initialValue: model.name}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="组件">
                    <a-input v-decorator="[ 'component', {initialValue: model.component}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="图标">
                    <a-input v-decorator="[ 'icon', {initialValue: model.icon}]">
                        <a-icon slot="addonBefore" v-if="model.icon" :type="model.icon" />
                        <a-icon
                            slot="addonAfter"
                            type="search"
                            @click="onSelectIcon"
                            style="cursor:pointer"
                        />
                    </a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="路径">
                    <a-input v-decorator="[ 'path', {initialValue: model.path}]" />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="跳转">
                    <a-input v-decorator="[ 'redirect', {initialValue: model.redirect}]" />
                </a-form-item>
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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="隐藏">
                    <a-switch v-model="model.hidden" v-decorator="[ 'hidden', {initialValue: model.hidden}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="缓存">
                    <a-switch v-model="model.keepAlive" v-decorator="[ 'keepAlive', {initialValue: model.keepAlive}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-textarea :rows="2" v-decorator="[ 'remark', {initialValue: model.remark}]" />
                </a-form-item>
            </a-form>
            <icon-selector-modal ref="iconModal" @change="onIconChange" />
        </a-spin>
    </e-drawer>
</template>

<script>
import { EDrawer, IconSelectorModal } from '@/components';

const url = '/sys/menu';

export default {
    components: {
        EDrawer,
        IconSelectorModal
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
            this.title = '添加菜单';
            if (record && record !== undefined) {
                this.model = { parentId: record.id, parentName: record.title };
                this.url = url + '/' + record.id + '/item';
            } else {
                this.model = { parentId: 0, parentName: '一级菜单' };
                this.url = url;
            }

            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑菜单信息';
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
        },
        onSelectIcon: function () {
            this.$refs.iconModal.show(this.model.icon);
        },
        onIconChange: function (icon) {
            this.model.icon = icon;
            this.form.setFieldsValue({ icon: icon });
        }
    }
};
</script>

<style scoped>
</style>
