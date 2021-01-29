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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="功能名称">
                    <a-input
                        v-decorator="[ 'name', {initialValue: model.name, rules: [ { required: true, message: '请输入功能名称' }] }]"
                    />
                </a-form-item>
                <a-row v-for="(k, index) in adTypeList" :key="index">
                    <a-col :md="12" :sm="24">
                    <a-form-item :label="index === 0 ? '广告类型' : ''" :required="true" :labelCol="{ span: 10 }" :wrapperCol="{ span: 14 ,offset: index>0?10:0 }">
                            <a-select placeholder="请选择广告类型" v-decorator="[adTypeList[index].id, { initialValue: adTypeList[index].value, rules: [ { required: true, message: '请选择广告类型' }] }]">
                                <a-select-option
                                    v-for="item in adTypeSelectList"
                                    :key="item.value"
                                    :value="item.value">
                                    {{ item.label }}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="24" :offset="1" class="dynamic-delete-button">
                        <a-icon v-if="adTypeList.length > 1" type="minus-circle-o" @click="remove(index)" />
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :md="12" :sm="24" :offset="5">
                        <a-button
                            icon="plus"
                            @click="addList"
                        >新增</a-button>
                    </a-col>
                </a-row>
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
            adTypeList: [],
            func: () => {}
        };
    },
    mounted () {
    },
    computed: {
        adTypeSelectList: function () {
            return this.$DictFilterExclude(this.$AdType, [0]);
        }
    },
    methods: {
        add: function (currentApp) {
            this.title = '添加功能';
            this.model = {};
            this.model.app = currentApp.key;
            this.url = '/app/function';
            this.func = this.$http.post;
            this.confirmLoading = false;
            this.visible = true;
        },
        edit: function (record, currentApp) {
            this.title = '编辑功能:' + record.id;
            this.model = record;
            this.model.app = currentApp.key;
            console.log(record.adTypeList);
            // this.adTypeList = JSON.parse(record.adTypeList);
            this.convertList(record.adTypeList);
            console.log(this.adTypeList);
            this.url = '/app/function/' + record.id;
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
        convertList(params) {
            this.adTypeList = [];
            params.forEach((item, index) => {
                item = JSON.parse(item);
                console.log(item);
                this.adTypeList.push(item);
            });
            console.log(this.adTypeList);
        },
        addList () {
            // 取数组的最后一项，默认是‘adType0’
            const item = this.adTypeList[this.adTypeList.length - 1].id;
            // 去掉adType然后+1，这样保证新增的项始终不会和其他项重复，且有规律
            const maxNum = parseInt(item.replace('adType-', '')) + 1;
            this.form.getFieldDecorator('adType-' + maxNum, {
                        placeholder: '请选择',
                        initialValue: '',
                        preserve: true
            });
            const newId = 'adType-' + maxNum;
            const jsonStr = '{"id": "' + newId + '","value": 0}';
            this.adTypeList.push(JSON.parse(jsonStr));
            // console.log(this.adTypeList);
        },
        remove (k) {
            const deleteObj = this.adTypeList.splice(k, 1);
            const deleteItem = deleteObj[0];
            // 给删除的那一项赋值为空
            this.form.setFieldsValue({
                [deleteItem]: ''
            });
            // 给删除的那一项设置为不必填，否则保存的时候过不去
            this.form.getFieldDecorator([deleteItem], {
                required: false
            });
        },
        onSubmit: function () {
            const $self = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    let str = '';
                    if (this.adTypeList.length > 1) {
                        // 循环adTypeList，将里面的值字符串拼起来
                        this.adTypeList.forEach((item, i) => {
                                if (i < this.adTypeList.length - 1) {
                                    str = str + '{"id":"' + item.id + '","value": ' + this.form.getFieldValue(item.id) + '},';
                                } else {
                                    str = str + '{"id":"' + item.id + '","value": ' + this.form.getFieldValue(item.id) + '}';
                                }
                        });
                    } else {
                        str = '{"id":"' + this.adTypeList[0].id + '","value": ' + this.form.getFieldValue(this.adTypeList[0].id) + '}';
                    }
                    str = '[' + str + ']';
                    console.log('JSON数组' + str);
                    values.adTypeList = str;
                    console.log(values);

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
    .dynamic-delete-button {
        margin-top: 8px;
    }
</style>
