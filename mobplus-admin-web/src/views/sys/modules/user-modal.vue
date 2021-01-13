<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="用户账号">
                    <a-input
                        v-decorator="[ 'userName', {initialValue: model.userName, rules: [ { required: true, message: '请输入用户账号' }] }]"
                        :readOnly="model.id!=null"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="用户昵称">
                    <a-input
                        v-decorator="[ 'displayName', {initialValue: model.displayName, rules: [ { required: true, message: '请输入用户昵称' }] }]"
                    />
                </a-form-item>
                <a-form-item
                    :labelCol="labelCol"
                    v-action="['sys:user:role']"
                    :wrapperCol="wrapperCol"
                    label="角色分配"
                >
                    <a-select
                        mode="multiple"
                        placeholder="==请选择角色=="
                        v-decorator="[ 'roles', {initialValue: model.roles}]"
                    >
                        <a-select-option
                            v-for="role in roles"
                            :key="role.id"
                            :value="role.id"
                        >{{ role.name }}</a-select-option>
                    </a-select>
                </a-form-item>
                <!-- <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="部门">
                    <a-tree-select
                        v-decorator="[ 'dptId', {initialValue: model.dptId+'' }]"
                        :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
                        :treeData="deptTreeData"
                        placeholder="==请选择=="
                        treeDefaultExpandAll
                    />
                </a-form-item>-->
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="姓名">
                    <a-input v-decorator="[ 'name', {initialValue: model.name}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="头像">
                    <a-avatar
                        shape="square"
                        :src="model.avatar"
                        @click="onSelectAvatar"
                        style="cursor:pointer"
                    />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="性别">
                    <a-select
                        v-decorator="['gender', {initialValue: model.gender}]"
                        placeholder="==请选择性别=="
                    >
                        <a-select-option :value="1">男</a-select-option>
                        <a-select-option :value="0">女</a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="电话号码">
                    <a-input v-decorator="[ 'mobile', {initialValue: model.mobile}]" />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="邮箱">
                    <a-input v-decorator="[ 'email', {initialValue: model.email}]" />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="生日">
                    <a-date-picker
                        placeholder="请选择出生日期"
                        v-decorator="['birthday', {initialValue: model.birthday}]"
                    />
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
                    <a-select
                        placeholder="==请选择状态=="
                        v-decorator="['status', {initialValue: model.status || 2, rules: [{ required: true, message: '请选择状态'}]} ]"
                    >
                        <a-select-option
                            v-for="item in Status"
                            :key="item.value"
                            :value="item.value"
                        >{{ item.label }}</a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                    <a-textarea v-decorator="[ 'remark', {initialValue: model.remark}]" :rows="4" />
                </a-form-item>
            </a-form>
        </a-spin>
        <avatar-selector-modal ref="avatarModal" @change="onAvatarChange" />
    </e-drawer>
</template>

<script>
import moment from 'moment';
import { EDrawer, AvatarSelectorModal } from '@/components';

const url = '/sys/user';

export default {
    name: 'SysUserModal',
    components: {
        EDrawer,
        AvatarSelectorModal
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

            roles: [],
            deptTreeData: [],

            url: url,
            func: () => {}
        };
    },
    mounted () {
        // this.loadDeptTreeData()
        this.loadRoleList();
    },
    computed: {
        Status: function () {
            return this.$DictFilter(this.$GeneralStatus, [2, 3, 5]);
        }
    },
    methods: {
        add: function () {
            this.title = '添加用户';
            this.model = {
                status: 2,
                dptId: '',
                avatar: 'https://randomuser.me/api/portraits/women/2.jpg'
            };
            this.url = url;
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record) {
            this.title = '编辑用户';
            if (record.birthday) {
                record.birthday = moment(record.birthday, this.dateFormat);
            }
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
                    values.avatar = $self.model.avatar;
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
        onSelectAvatar: function () {
            this.$refs.avatarModal.show(this.model.avatar || '');
        },
        onAvatarChange: function (avatar) {
            this.model.avatar = avatar;
        },
        // loadDeptTreeData: async function() {
        //     let result = await this.$http.get('/sys/depart/tree', {})
        //     this.deptTreeData = this.convertTreeData(result)
        // },
        loadRoleList: async function () {
            this.roles = await this.$http.get('/sys/role/sct', {});
        },
        convertTreeData: function (data) {
            return data.map(ele => {
                const item = {
                    title: ele.name,
                    value: ele.id + '',
                    key: ele.id + ''
                };
                if (ele.children) {
                    item.children = this.convertTreeData(ele.children);
                }
                return item;
            });
        }
    }
};
</script>

<style scoped>
</style>
