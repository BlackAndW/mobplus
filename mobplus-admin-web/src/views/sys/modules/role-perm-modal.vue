<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form class="menu-perm-selector">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="角色名称">
                    <a-input v-model="model.name" :readOnly="true" />
                </a-form-item>
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    v-for="(permission, index) in permissions"
                    :key="index"
                    :label="permission.name"
                >
                    <template>
                        <a-checkbox
                            v-if="
                                permission.actionsOptions.length > 0 &&
                                    editData == true
                            "
                            :indeterminate="permission.indeterminate"
                            :checked="permission.checkedAll"
                            @change="onChangeCheckAll($event, permission)"
                        >全选</a-checkbox>
                        <a-checkbox-group
                            :disabled="editData == false"
                            :options="permission.actionsOptions"
                            v-model="permission.selected"
                            @change="onChangeCheck(permission)"
                        />
                    </template>
                </a-form-item>
            </a-form>
        </a-spin>
    </e-drawer>
</template>

<script>
import { EDrawer } from '@/components';

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

            editData: true,
            model: {},
            permissions: []
        };
    },
    methods: {
        show: function (record) {
            this.title = '查看授权';
            this.model = record;
            this.editData = false;
            this.visible = true;
            this.loadDataList(record.id);
        },
        edit: function (record) {
            this.title = '角色授权';
            this.model = record;
            this.editData = true;
            this.visible = true;
            this.loadDataList(record.id);
        },
        close: function (success) {
            this.$emit('close', success || false);
            this.visible = false;
        },
        onCancel: function () {
            this.close(false);
        },
        onChangeCheck (permission) {
            permission.indeterminate =
                !!permission.selected.length &&
                permission.selected.length < permission.actionsOptions.length;
            permission.checkedAll =
                permission.selected.length === permission.actionsOptions.length;
        },
        onChangeCheckAll (e, permission) {
            Object.assign(permission, {
                selected: e.target.checked
                    ? permission.actionsOptions.map(obj => obj.value)
                    : [],
                indeterminate: false,
                checkedAll: e.target.checked
            });
        },
        loadDataList: function (roleId) {
            this.confirmLoading = true;
            this.$http
                .get('/sys/role/' + roleId + '/perm')
                .then(data => {
                    this.permissions = data;
                    this.confirmLoading = false;
                })
                .catch(err => {
                    if (err) {
                        console.log(err.stack);
                    }
                    this.confirmLoading = false;
                });
        },
        onSubmit: function () {
            if (this.editData === false) {
                this.close(false);
                return;
            }
            const $self = this;
            let selected = [];
            this.permissions.map(ele => {
                selected = selected.concat(ele.selected);
            });
            console.log(selected);
            this.confirmLoading = true;
            this.$http
                .post('/sys/role/' + this.model.id + '/perm', selected)
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
    }
};
</script>

<style lang="less" scoped>
.menu-perm-selector {
    .ant-form-item {
        margin-bottom: 0px;
    }
}
</style>
