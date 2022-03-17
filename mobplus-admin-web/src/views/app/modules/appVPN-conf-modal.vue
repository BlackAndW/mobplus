<template>
    <e-drawer :visible="visible" :title="title" @cancel="onCancel" @ok="onSubmit">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <!-- <a-form-item>
                    <a-input
                        type="hidden"
                        v-decorator="[ 'id', {initialValue: model.b01}]"
                    />
                </a-form-item> -->

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="国家排序：">
                    <a-select
                        mode="multiple"
                        placeholder="按从左往右排"
                        v-decorator="[ 'order', { initialValue: order } ]"
                    >
                        <a-select-option
                            v-for="item in countryList"
                            :key="item.name"
                            :value="item.value"
                        >{{ item.name }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="普通节点">
                    <a-checkbox :indeterminate="indeterminateNM" :checked="checkAllServerNM" @change="onServerNMCheckAllChange">
                        全选
                    </a-checkbox><br />
                    <a-checkbox-group
                        @change="onServerNMChange"
                        v-decorator="[ 'serverNMCheckList', {initialValue: serverNMCheckList, rules: [ { required: true, message: '请选择普通节点' }] }]">
                        <a-row class="checkbox-width">
                            <a-col
                                v-for="server in serverNMList"
                                :key="server.b01"
                                span="8">
                                    <a-checkbox
                                        :value="server.b01"
                                        >{{ server.b02 }}
                                    </a-checkbox>
                            </a-col>
                        </a-row>
                    </a-checkbox-group>
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="VIP节点">
                    <a-checkbox :indeterminate="indeterminateVIP" :checked="checkAllServerVIP" @change="onServerVIPCheckAllChange">
                        全选
                    </a-checkbox><br />
                    <a-checkbox-group
                        @change="onServerVIPChange"
                        v-decorator="[ 'serverVIPCheckList', {initialValue: serverVIPCheckList, rules: [ { required: true, message: '请选择VIP节点' }] }]">
                        <a-row class="checkbox-width">
                            <a-col
                                v-for="server in serverVIPList"
                                :key="server.b01"
                                span="8">
                                    <a-checkbox
                                        :value="server.b01"
                                        >{{ server.b02 }}
                                    </a-checkbox>
                            </a-col>
                        </a-row>
                    </a-checkbox-group>
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备用节点">
                    <a-checkbox :indeterminate="indeterminateBK" :checked="checkAllServerBK" @change="onServerBKCheckAllChange">
                        全选
                    </a-checkbox><br />
                    <a-checkbox-group
                        @change="onServerBKChange"
                        v-decorator="[ 'serverBKCheckList', {initialValue: serverBKCheckList, rules: [ { required: true, message: '请选择备用节点' }] }]">
                        <a-row class="checkbox-width">
                            <a-col
                                v-for="server in serverBKList"
                                :key="server.b01"
                                span="8">
                                    <a-checkbox
                                        :value="server.b01"
                                        >{{ server.b02 }}
                                    </a-checkbox>
                            </a-col>
                        </a-row>
                    </a-checkbox-group>
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
            serverNMList: [],
            serverNMCheckList: [],
            indeterminateNM: true,
            checkAllServerNM: false,
            serverVIPList: [],
            serverVIPCheckList: [],
            indeterminateVIP: true,
            checkAllServerVIP: false,
            serverBKList: [],
            serverBKCheckList: [],
            indeterminateBK: true,
            checkAllServerBK: false,
            order: '',
            name_en: '',
            summary_en: '',
            func: () => {}
        };
    },
    mounted () { },
    computed: {},
    methods: {
        query: function (currentApp) {
            this.loadAllServer(currentApp);
            this.$http.get('/app/vpn/setting/' + currentApp.key)
                .then(res => {
                    this.order = res.order;
                    this.serverNMCheckList = res.normalServerList;
                    this.serverVIPCheckList = res.vipServerList;
                    this.serverBKCheckList = res.backServerList;
                });
        },
        edit: function (currentApp) {
            this.query(currentApp);
            this.title = '编辑:' + currentApp.key;
            // this.model = record;
            this.url = '/app/vpn/setting/' + currentApp.key;
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
        loadAllServer (currentApp) {
            this.$http.get('/app/vpn/s/' + currentApp.key)
                .then(res => {
                    this.serverNMList = res.serverNMList;
                    this.serverVIPList = res.serverVIPList;
                    this.serverBKList = res.serverBKList;
                });
        },
        onServerNMChange () {
            this.$nextTick(() => {
                const checkedList = this.form.getFieldValue('serverNMCheckList');
                this.indeterminateNM = !!checkedList.length && checkedList.length < this.serverNMList.length;
                this.checkAllServerNM = checkedList.length === this.serverNMList.length;
            });
        },
        onServerNMCheckAllChange (e) {
            const checkAllList = [];
            this.serverNMList.forEach(item => {
                checkAllList.push(item.b01);
            });
            this.form.setFieldsValue({
                'serverNMCheckList': e.target.checked ? checkAllList : []
            });
            this.indeterminateNM = false;
            this.checkAllServerNM = e.target.checked;
        },

        onServerVIPChange () {
            this.$nextTick(() => {
                const checkedList = this.form.getFieldValue('serverVIPCheckList');
                this.indeterminateVIP = !!checkedList.length && checkedList.length < this.serverVIPList.length;
                this.checkAllServerVIP = checkedList.length === this.serverVIPList.length;
            });
        },
        onServerVIPCheckAllChange (e) {
            const checkAllList = [];
            this.serverVIPList.forEach(item => {
                checkAllList.push(item.b01);
            });
            this.form.setFieldsValue({
                'serverVIPCheckList': e.target.checked ? checkAllList : []
            });
            this.indeterminateVIP = false;
            this.checkAllServerVIP = e.target.checked;
        },

        onServerBKChange () {
            this.$nextTick(() => {
                const checkedList = this.form.getFieldValue('serverBKCheckList');
                this.indeterminateBK = !!checkedList.length && checkedList.length < this.serverBKList.length;
                this.checkAllServerBK = checkedList.length === this.serverBKList.length;
            });
        },
        onServerBKCheckAllChange (e) {
            const checkAllList = [];
            this.serverBKList.forEach(item => {
                checkAllList.push(item.b01);
            });
            this.form.setFieldsValue({
                'serverBKCheckList': e.target.checked ? checkAllList : []
            });
            this.indeterminateBK = false;
            this.checkAllServerBK = e.target.checked;
        },

        onSubmit: function () {
            const $self = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    $self.confirmLoading = true;
                    // console.log(values);
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
    .checkbox-width {
        width: 400px;
    }
</style>
