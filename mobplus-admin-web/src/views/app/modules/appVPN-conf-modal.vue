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
            order: '',
            name_en: '',
            summary_en: '',
            func: () => {}
        };
    },
    mounted () {
    },
    computed: {},
    methods: {
        query: function (currentApp) {
            this.$http.get('/app/vpn/conf/' + currentApp.key)
            .then(res => {
                console.log(res);
                this.order = res.data;
            });
        },
        edit: function (currentApp) {
            this.query(currentApp);
            this.title = '编辑:' + currentApp.key;
            // this.model = record;
            this.url = '/app/vpn/conf/' + currentApp.key;
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
                    console.log(values);
                    $self
                        .func($self.url, values)
                        .then(data => {
                            console.log(data);
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
