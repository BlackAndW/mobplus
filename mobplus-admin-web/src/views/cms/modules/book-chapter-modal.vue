<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'bookId', {initialValue: model.bookId}]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="章节名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入书籍名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="章节序号">
                    <a-input
                        v-decorator="[ 'chapterNo', {initialValue: model.chapterNo, rules: [ { required: true, message: '请输入书籍名称' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="章节内容：">
                    <a-input
                        type="textarea"
                        row="{10}"
                        v-decorator="[ 'content', {initialValue: model.content }]"
                    />
                </a-form-item>
                <a-form-item v-if="isVip == 0" :labelCol="labelCol" :wrapperCol="wrapperCol" label="章节类型">
                    <a-radio-group button-style="solid" v-decorator="[ 'isLock', {initialValue: model.isLock || 0, rules: [ { required: true, message: '请选择章节类型' }]}]">
                        <a-radio-button :value="1">需要解锁</a-radio-button>
                        <a-radio-button :value="0">无需解锁</a-radio-button>
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
            appId: '',
            title: '',
            visible: false,
            confirmLoading: false,
            form: this.$form.createForm(this),
            model: {},
            isVip: null,
            loading: false,
            func: () => {}
        };
    },
    computed: { },
    methods: {
        add: function (bookId) {
            this.title = '新增章节';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.model = {};
            this.model.bookId = bookId;
            this.url = '/cms/book/chapter';
            this.visible = true;
        },
        edit: function (record, bookId) {
            this.title = '编辑章节:' + record.name;
            this.model = record;
            this.model.bookId = bookId;
            this.url = '/cms/book/chapter/' + record.id;
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
</style>
