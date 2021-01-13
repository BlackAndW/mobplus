<template>
<div>
    <a-select v-decorator="[ prop, {initialValue: value}]" @change="handleChange">
        <a-select-option v-for="item in data" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
    </a-select>
</div>
</template>

<script>
export default {
    props: {
        prop: { type: String, default: '' },
        // eslint-disable-next-line vue/require-prop-type-constructor
        value: { type: String | Number, default: -1 },
        // eslint-disable-next-line vue/require-default-prop
        data: { type: Array }
    },
    data () {
        return {};
    },
    computed: {},
    mounted () {
    },
    methods: {
        handleChange: function (value, options) {
            this.$emit('change', value, options);
        }
    }
};
</script>

<style lang="less" scoped>
</style>
