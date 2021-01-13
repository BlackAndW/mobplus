<script>
const GeneralStatus = {
    1: { label: '待审核', status: 'processing' },
    2: { label: '正常', status: 'success' },
    3: { label: '受限', status: 'warning' },
    4: { label: '拒绝', status: 'error' },
    5: { label: '禁用', status: 'error' },
    0: { label: '未知', status: 'default' }
};
export default {
    props: {
        // eslint-disable-next-line vue/require-prop-type-constructor
        value: { type: String | Number, default: -1 },
        // eslint-disable-next-line vue/require-valid-default-prop
        items: { type: Array, default: [] }
    },
    methods: {
        renderData: function () {
            if (this.items && this.items.length > 0) {
                for (let idx = 0; idx < this.items.length; idx++) {
                    if (this.items[idx].value === this.value) {
                        return this.items[idx];
                    }
                }
                return GeneralStatus[0];
            }
            let item = GeneralStatus[this.value];
            if (!item) {
                item = GeneralStatus[0];
            }
            return item;
        }
    },
    render () {
        const renderData = this.renderData();
        renderData.status = renderData.status || 'success';
        return (
            <a-badge status={renderData.status} text={renderData.label} />
        );
    }
};
</script>

<style lang="less" scoped>
</style>
